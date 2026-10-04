package br.com.unicos.ms_funcionario.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.context.UserContext;
import br.com.unicos.ms_funcionario.dto.cargo.CargoRequest;
import br.com.unicos.ms_funcionario.dto.cargo.CargoResponse;
import br.com.unicos.ms_funcionario.mapper.CargoMapper;
import br.com.unicos.ms_funcionario.model.Cargo;
import br.com.unicos.ms_funcionario.repository.CargoRepository;
import br.com.unicos.ms_funcionario.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CargoService extends BaseTenantService<Cargo, Long> {

    private static final String MSG_DUPLICADO = "Já existe cargo com esse nome.";

    private final CargoRepository repository;
    private final FuncionarioRepository funcionarioRepository;
    private final CargoMapper mapper;

    public CargoService(CargoRepository repository, FuncionarioRepository funcionarioRepository, CargoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.funcionarioRepository = funcionarioRepository;
        this.mapper = mapper;
    }

    public CargoResponse salvar(CargoRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        if (repository.existsByNomeIgnoreCaseAndEmpresaId(request.nome().trim(), empresaId))
            throw new IllegalArgumentException(MSG_DUPLICADO);

        Cargo entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);

        return mapper.toResponse(save(entity));
    }

    public CargoResponse atualizar(Long id, CargoRequest request) {
        Long empresaId = TenantContext.getEmpresaId();
        Cargo entity = buscar(id);

        if (repository.existsByNomeIgnoreCaseAndEmpresaIdAndIdNot(request.nome().trim(), empresaId, id))
            throw new IllegalArgumentException(MSG_DUPLICADO);

        // Impede que o usuário amplie o próprio acesso (ex.: transformando o cargo de vendedor em gerente).
        if (entity.getPapel() != request.papel()
                && funcionarioRepository.existsByUsuarioIdAndCargoIdAndEmpresaId(UserContext.getUsuarioId(), id, empresaId))
            throw new IllegalStateException("O usuário não pode alterar o papel do próprio cargo.");

        mapper.updateEntity(entity, request);

        return mapper.toResponse(save(entity));
    }

    @Transactional(readOnly = true)
    public CargoResponse buscarPorId(Long id) {
        return mapper.toResponse(buscar(id));
    }

    @Transactional(readOnly = true)
    public Page<CargoResponse> listar(Pageable pageable) {
        return findAllByEmpresaId(TenantContext.getEmpresaId(), pageable).map(mapper::toResponse);
    }

    public void deletar(Long id) {
        Cargo entity = buscar(id);

        if (funcionarioRepository.existsByCargoIdAndEmpresaId(id, TenantContext.getEmpresaId()))
            throw new IllegalStateException("O cargo possui funcionários vinculados e não pode ser excluído.");

        repository.delete(entity);
    }

    private Cargo buscar(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado: " + id));
    }
}
