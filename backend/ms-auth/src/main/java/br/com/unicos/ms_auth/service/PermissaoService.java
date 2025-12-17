package br.com.unicos.ms_auth.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_auth.dto.permissao.PermissaoRequest;
import br.com.unicos.ms_auth.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_auth.mapper.PermissaoMapper;
import br.com.unicos.ms_auth.model.Permissao;
import br.com.unicos.ms_auth.repository.PermissaoRepository;
import br.com.unicos.ms_auth.repository.RolePermissaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do serviço responsável pelas regras de negócio
 * relacionadas à entidade {@link Permissao}.
 */
@Service
public class PermissaoService extends BaseTenantService<Permissao, Long> {

    private final PermissaoRepository repository;
    private final RolePermissaoRepository rolePermissaoRepository;
    private final PermissaoMapper mapper;

    public PermissaoService(PermissaoRepository repository, RolePermissaoRepository rolePermissaoRepository, PermissaoMapper mapper) {
        super(repository);
        this.repository = repository;
        this.rolePermissaoRepository = rolePermissaoRepository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    public PermissaoResponse salvar(PermissaoRequest request) {
        validarNomeDuplicado(request.nome());

        Permissao entity = Permissao.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();

        return mapper.toResponse(save(entity));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Transactional
    public PermissaoResponse atualizar(Long id, PermissaoRequest request) {
        Permissao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: " + id));

        if (!entity.getNome().equalsIgnoreCase(request.nome())) {
            validarNomeDuplicado(request.nome());
            entity.setNome(request.nome());
        }

        entity.setDescricao(request.descricao());

        return mapper.toResponse(save(entity));
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Transactional(readOnly = true)
    public PermissaoResponse buscarPorId(Long id) {
        Permissao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: " + id));
        return mapper.toResponse(entity);
    }

    // ============================================================
    // LISTAGEM ADMINISTRATIVA (PAGINADA)
    // ============================================================

    @Transactional(readOnly = true)
    public Page<PermissaoResponse> listar(String nome, Pageable pageable) {
        verificarPermissao("PERMISSAO_LISTAR");

        Page<Permissao> page;

        if (nome == null || nome.isBlank())
            page = findAllByEmpresaId(TenantContext.getEmpresaId(), pageable);
        else
            page = repository.findByNomeContainingIgnoreCaseAndEmpresaId(nome, TenantContext.getEmpresaId(), pageable);

        return page.map(mapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void deletar(Long id) {
        if (!existsById(id))
            throw new EntityNotFoundException("Permissão não encontrada: " + id);
        repository.deleteById(id);
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private void validarNomeDuplicado(String nome) {
        if (repository.existsByNomeContainingIgnoreCaseAndEmpresaId(nome, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe uma permissão cadastrada com o nome informado.");
    }

    public void verificarPermissao(String nomePermissao) {
        if (!rolePermissaoRepository.usuarioPossuiPermissao(TenantContext.getUsuarioId(), TenantContext.getEmpresaId(), nomePermissao))
            throw new AccessDeniedException("Usuário não possui permissão para executar a função desejada");
    }
}
