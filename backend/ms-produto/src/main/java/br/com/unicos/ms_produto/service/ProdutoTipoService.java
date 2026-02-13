package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoCreateRequest;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoResponse;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoResumoResponse;
import br.com.unicos.ms_produto.dto.produtotipo.ProdutoTipoUpdateRequest;
import br.com.unicos.ms_produto.mapper.ProdutoTipoMapper;
import br.com.unicos.ms_produto.model.ProdutoTipo;
import br.com.unicos.ms_produto.repository.ProdutoTipoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ProdutoTipoService extends BaseTenantService<ProdutoTipo, Long> {

    private final ProdutoTipoRepository repository;
    private final ProdutoTipoMapper mapper;

    public ProdutoTipoService(
            ProdutoTipoRepository repository,
            ProdutoTipoMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "produto-tipo-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoTipoResponse criar(ProdutoTipoCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarNomeDuplicado(request.nome(), empresaId);

        ProdutoTipo tipo = mapper.toEntity(request, empresaId);
        tipo.setAtivo(true);

        return mapper.toResponse(repository.save(tipo));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "produto-tipo-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoTipoResponse atualizar(Long id, ProdutoTipoUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoTipo tipo = buscarTipo(id, empresaId);

        if (!tipo.getNome().equalsIgnoreCase(request.nome()))
            validarNomeDuplicado(request.nome(), empresaId);

        mapper.updateEntity(request, tipo);

        return mapper.toResponse(repository.save(tipo));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-tipo-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoTipoResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarTipo(id, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-tipo-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ProdutoTipoResumoResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findAllByEmpresaId(empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-tipo-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ProdutoTipoResumoResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByAtivoAndEmpresaId(ativo, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // STATUS
    // ============================================================

    @CircuitBreaker(name = "produto-tipo-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoTipoResponse ativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoTipo tipo = buscarTipo(id, empresaId);
        tipo.setAtivo(true);

        return mapper.toResponse(repository.save(tipo));
    }

    @CircuitBreaker(name = "produto-tipo-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoTipoResponse inativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoTipo tipo = buscarTipo(id, empresaId);
        tipo.setAtivo(false);

        return mapper.toResponse(repository.save(tipo));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "produto-tipo-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarTipo(id, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ProdutoTipoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de tipos de produto temporariamente indisponível");
    }

    private Page<ProdutoTipoResumoResponse> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de tipos de produto temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de tipos de produto temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoTipo buscarTipo(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de produto não encontrado: " + id));
    }

    private void validarNomeDuplicado(String nome, Long empresaId) {
        if (repository.existsByNomeAndEmpresaId(nome, empresaId))
            throw new IllegalArgumentException("Já existe um tipo de produto com o nome informado.");
    }
}
