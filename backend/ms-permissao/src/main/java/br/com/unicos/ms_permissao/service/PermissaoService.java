package br.com.unicos.ms_permissao.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.core.usuario.auth.context.UserContext;
import br.com.unicos.ms_permissao.dto.permissao.PermissaoRequest;
import br.com.unicos.ms_permissao.dto.permissao.PermissaoResponse;
import br.com.unicos.ms_permissao.mapper.PermissaoMapper;
import br.com.unicos.ms_permissao.model.Permissao;
import br.com.unicos.ms_permissao.repository.PermissaoRepository;
import br.com.unicos.ms_permissao.repository.RoleUsuarioRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PermissaoService extends BaseTenantService<Permissao, Long> {

    private final PermissaoRepository repository;
    private final RoleUsuarioRepository roleUsuarioRepository;
    private final PermissaoMapper mapper;

    public PermissaoService(
            PermissaoRepository repository,
            RoleUsuarioRepository roleUsuarioRepository,
            PermissaoMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.roleUsuarioRepository = roleUsuarioRepository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @Transactional
    @CircuitBreaker(name = "permissao-admin", fallbackMethod = "fallbackAdmin")
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
    @CircuitBreaker(name = "permissao-admin", fallbackMethod = "fallbackAdmin")
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
    @CircuitBreaker(name = "permissao-admin", fallbackMethod = "fallbackAdmin")
    public PermissaoResponse buscarPorId(Long id) {
        Permissao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada: " + id));
        return mapper.toResponse(entity);
    }

    // ============================================================
    // LISTAGEM PAGINADA
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "permissao-admin", fallbackMethod = "fallbackAdmin")
    public Page<PermissaoResponse> listar(String nome, Pageable pageable) {
        Page<Permissao> page;

        if (nome == null || nome.isBlank()) {
            page = findAllByEmpresaId(TenantContext.getEmpresaId(), pageable);
        } else {
            page = repository.findByNomeContainingIgnoreCaseAndEmpresaId(
                    nome,
                    TenantContext.getEmpresaId(),
                    pageable
            );
        }

        return page.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "permissao-admin", fallbackMethod = "fallbackAdminPermissao")
    public boolean usuarioPossuiPermissao(String nomePermissao) {
        return roleUsuarioRepository.usuarioPossuiPermissao(TenantContext.getEmpresaId(), UserContext.getUsuarioId(), nomePermissao);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "permissao-admin", fallbackMethod = "fallbackAdminVoid")
    public void deletar(Long id) {
        if (!existsById(id))
            throw new EntityNotFoundException("Permissão não encontrada: " + id);
        repository.deleteById(id);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private PermissaoResponse fallbackAdmin(Object request, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de permissões temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de permissões temporariamente indisponível");
    }

    private boolean fallbackAdminPermissao(String nomePermissao, Throwable ex) {
        return false;
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private void validarNomeDuplicado(String nome) {
        if (repository.existsByNomeContainingIgnoreCaseAndEmpresaId(
                nome,
                TenantContext.getEmpresaId()
        ))
            throw new IllegalArgumentException("Já existe uma permissão cadastrada com o nome informado.");
    }

}
