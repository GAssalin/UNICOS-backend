package br.com.unicos.ms_funcionario.service;

import br.com.unicos.core.funcionario.dto.AcessoCarteiraResponse;
import br.com.unicos.core.funcionario.enums.EscopoCarteira;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.context.UserContext;
import br.com.unicos.ms_funcionario.client.PessoaService;
import br.com.unicos.ms_funcionario.dto.funcionario.FuncionarioRequest;
import br.com.unicos.ms_funcionario.dto.funcionario.FuncionarioResponse;
import br.com.unicos.ms_funcionario.enums.PapelFuncionario;
import br.com.unicos.ms_funcionario.enums.StatusFuncionario;
import br.com.unicos.ms_funcionario.mapper.FuncionarioMapper;
import br.com.unicos.ms_funcionario.model.Cargo;
import br.com.unicos.ms_funcionario.model.Funcionario;
import br.com.unicos.ms_funcionario.repository.CargoRepository;
import br.com.unicos.ms_funcionario.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class FuncionarioService extends BaseTenantService<Funcionario, Long> {

    /**
     * Limite de níveis percorridos ao validar a hierarquia, protegendo contra dados inconsistentes.
     */
    private static final int MAX_NIVEIS_HIERARQUIA = 100;

    private final FuncionarioRepository repository;
    private final CargoRepository cargoRepository;
    private final FuncionarioMapper mapper;
    private final PessoaService pessoaService;

    public FuncionarioService(
            FuncionarioRepository repository,
            CargoRepository cargoRepository,
            FuncionarioMapper mapper,
            PessoaService pessoaService
    ) {
        super(repository);
        this.repository = repository;
        this.cargoRepository = cargoRepository;
        this.mapper = mapper;
        this.pessoaService = pessoaService;
    }

    public FuncionarioResponse salvar(FuncionarioRequest request) {
        Cargo cargo = buscarCargo(request.cargoId());
        Long superiorId = validarSuperior(null, request.superiorId());
        validar(null, request);

        Funcionario entity = mapper.toEntity(request);
        entity.setEmpresaId(TenantContext.getEmpresaId());
        entity.setCargo(cargo);
        entity.setSuperiorId(superiorId);

        return mapper.toResponse(save(entity));
    }

    public FuncionarioResponse atualizar(Long id, FuncionarioRequest request) {
        Funcionario entity = buscar(id);
        validarAlteracaoDoProprioCadastro(entity, request);

        Cargo cargo = buscarCargo(request.cargoId());
        Long superiorId = validarSuperior(entity, request.superiorId());
        validar(entity, request);

        mapper.updateEntity(entity, request);
        entity.setCargo(cargo);
        entity.setSuperiorId(superiorId);

        return mapper.toResponse(save(entity));
    }

    @Transactional(readOnly = true)
    public FuncionarioResponse buscarPorId(Long id) {
        return mapper.toResponse(buscar(id));
    }

    /**
     * Cadastro de funcionário do usuário autenticado.
     */
    @Transactional(readOnly = true)
    public FuncionarioResponse buscarDoUsuarioAtual() {
        return repository.findByUsuarioIdAndEmpresaId(UserContext.getUsuarioId(), TenantContext.getEmpresaId())
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("O usuário não possui cadastro de funcionário."));
    }

    @Transactional(readOnly = true)
    public Page<FuncionarioResponse> listar(StatusFuncionario status, PapelFuncionario papel, Pageable pageable) {
        return repository.pesquisar(TenantContext.getEmpresaId(), status, papel, pageable)
                .map(mapper::toResponse);
    }

    /**
     * Subordinados diretos do funcionário.
     */
    @Transactional(readOnly = true)
    public Page<FuncionarioResponse> listarSubordinados(Long id, Pageable pageable) {
        buscar(id);
        return repository.findBySuperiorIdAndEmpresaId(id, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResponse);
    }

    public void deletar(Long id) {
        Funcionario entity = buscar(id);

        if (UserContext.getUsuarioId().equals(entity.getUsuarioId()))
            throw new IllegalStateException("O usuário não pode excluir o próprio cadastro de funcionário.");

        if (repository.existsBySuperiorIdAndEmpresaId(id, TenantContext.getEmpresaId()))
            throw new IllegalStateException(
                    "O funcionário é superior de outros funcionários. Defina outro superior para eles antes de excluí-lo.");

        repository.delete(entity);
    }

    /**
     * Acesso do usuário à carteira de clientes na empresa do contexto atual.
     *
     * <p>
     * Quem não é funcionário da empresa (ex.: administradores da plataforma) não tem a carteira
     * restringida: o acesso aos clientes depende apenas das permissões da role. Também não pode
     * ser indicado como vendedor responsável por clientes.
     * </p>
     */
    @Transactional(readOnly = true)
    public AcessoCarteiraResponse buscarAcessoCarteira(Long usuarioId) {
        return repository.findByUsuarioIdAndEmpresaId(usuarioId, TenantContext.getEmpresaId())
                .map(funcionario -> new AcessoCarteiraResponse(
                        usuarioId,
                        funcionario.getId(),
                        funcionario.getEscopoCarteira(),
                        !funcionario.isDesligado()
                ))
                .orElseGet(() -> new AcessoCarteiraResponse(usuarioId, null, EscopoCarteira.TODAS, false));
    }

    // ============================================================
    // AUX
    // ============================================================

    private Funcionario buscar(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado: " + id));
    }

    private Cargo buscarCargo(Long id) {
        return cargoRepository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado: " + id));
    }

    /**
     * Impede que o usuário amplie o próprio acesso à carteira (ex.: promovendo-se de vendedor a
     * gerente ou revertendo o próprio desligamento) ou transfira o vínculo do próprio cadastro.
     */
    private void validarAlteracaoDoProprioCadastro(Funcionario entity, FuncionarioRequest request) {
        if (!UserContext.getUsuarioId().equals(entity.getUsuarioId()))
            return;

        if (!entity.getCargo().getId().equals(request.cargoId())
                || !entity.getUsuarioId().equals(request.usuarioId())
                || entity.getStatus() != request.status())
            throw new IllegalStateException(
                    "O usuário não pode alterar o cargo, o status nem o usuário vinculado ao próprio cadastro de funcionário.");
    }

    /**
     * Valida o superior informado quando ele muda: deve ser outro funcionário da empresa, não
     * desligado e que não esteja abaixo do próprio funcionário na hierarquia.
     */
    private Long validarSuperior(Funcionario atual, Long superiorId) {
        if (superiorId == null || (atual != null && superiorId.equals(atual.getSuperiorId())))
            return superiorId;

        if (atual != null && superiorId.equals(atual.getId()))
            throw new IllegalArgumentException("O funcionário não pode ser superior de si mesmo.");

        Funcionario superior = findById(superiorId)
                .orElseThrow(() -> new EntityNotFoundException("Superior não encontrado: " + superiorId));

        if (superior.isDesligado())
            throw new IllegalArgumentException("O superior informado está desligado.");

        if (atual != null)
            validarHierarquiaSemCiclo(atual.getId(), superior);

        return superiorId;
    }

    private void validarHierarquiaSemCiclo(Long funcionarioId, Funcionario superior) {
        Long proximo = superior.getSuperiorId();

        for (int nivel = 1; proximo != null; nivel++) {
            if (proximo.equals(funcionarioId))
                throw new IllegalArgumentException(
                        "Hierarquia circular: o superior informado está subordinado a este funcionário.");

            if (nivel >= MAX_NIVEIS_HIERARQUIA)
                throw new IllegalStateException(
                        "A hierarquia de funcionários excede o limite de " + MAX_NIVEIS_HIERARQUIA + " níveis.");

            proximo = findById(proximo).map(Funcionario::getSuperiorId).orElse(null);
        }
    }

    /**
     * Datas, unicidade (pessoa, usuário e matrícula) e existência da pessoa e do usuário no
     * ms-pessoas. As consultas remotas são feitas por último e apenas para valores alterados.
     */
    private void validar(Funcionario atual, FuncionarioRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        if (request.dataDesligamento() != null && request.dataDesligamento().isBefore(request.dataAdmissao()))
            throw new IllegalArgumentException("A data de desligamento não pode ser anterior à data de admissão.");

        if (request.status() == StatusFuncionario.DESLIGADO && request.dataDesligamento() == null)
            throw new IllegalArgumentException("Informe a data de desligamento do funcionário desligado.");

        boolean pessoaAlterada = atual == null || !request.pessoaId().equals(atual.getPessoaId());
        boolean usuarioAlterado = request.usuarioId() != null
                && (atual == null || !request.usuarioId().equals(atual.getUsuarioId()));
        String matricula = FuncionarioMapper.normalizarMatricula(request.matricula());
        boolean matriculaAlterada = matricula != null
                && (atual == null || !Objects.equals(matricula, atual.getMatricula()));

        if (pessoaAlterada && repository.existsByPessoaIdAndEmpresaId(request.pessoaId(), empresaId))
            throw new IllegalArgumentException("Já existe funcionário para essa pessoa.");

        if (usuarioAlterado && repository.existsByUsuarioIdAndEmpresaId(request.usuarioId(), empresaId))
            throw new IllegalArgumentException("O usuário já está vinculado a outro funcionário.");

        if (matriculaAlterada && repository.existsByMatriculaAndEmpresaId(matricula, empresaId))
            throw new IllegalArgumentException("A matrícula já está em uso por outro funcionário.");

        if (pessoaAlterada)
            pessoaService.validarPessoa(request.pessoaId());

        if (usuarioAlterado)
            pessoaService.validarUsuario(request.usuarioId());
    }
}
