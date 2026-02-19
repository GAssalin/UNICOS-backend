package br.com.unicos.ms_vendas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_vendas.dto.produtoprecobase.ProdutoPrecoBaseCreateRequest;
import br.com.unicos.ms_vendas.dto.produtoprecobase.ProdutoPrecoBaseResponse;
import br.com.unicos.ms_vendas.dto.produtoprecobase.ProdutoPrecoBaseUpdateRequest;
import br.com.unicos.ms_vendas.mapper.ProdutoPrecoBaseMapper;
import br.com.unicos.ms_vendas.model.ProdutoPrecoBase;
import br.com.unicos.ms_vendas.repository.ProdutoPrecoBaseRepository;
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
public class ProdutoPrecoBaseService extends BaseTenantService<ProdutoPrecoBase, Long> {

    private final ProdutoPrecoBaseRepository repository;
    private final ProdutoPrecoBaseMapper mapper;

    public ProdutoPrecoBaseService(
            ProdutoPrecoBaseRepository repository,
            ProdutoPrecoBaseMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "produto-preco-base-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoPrecoBaseResponse criar(ProdutoPrecoBaseCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarDuplicidadeProduto(request.produtoId(), empresaId);

        ProdutoPrecoBase preco = mapper.toEntity(request, empresaId);
        preco.setAtivo(true);

        return mapper.toResponse(repository.save(preco));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "produto-preco-base-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoPrecoBaseResponse atualizar(Long id, ProdutoPrecoBaseUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoPrecoBase preco = buscarPreco(id, empresaId);

        mapper.updateEntity(request, preco);

        return mapper.toResponse(repository.save(preco));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-preco-base-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoPrecoBaseResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarPreco(id, empresaId));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-preco-base-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoPrecoBaseResponse buscarPorProduto(Long produtoId) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoPrecoBase preco = repository.findByProdutoIdAndEmpresaId(produtoId, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Preço base não encontrado para o produto: " + produtoId));

        return mapper.toResponse(preco);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-preco-base-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ProdutoPrecoBaseResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findAllByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-preco-base-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ProdutoPrecoBaseResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByAtivoAndEmpresaId(ativo, empresaId, pageable)
                .map(mapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "produto-preco-base-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarPreco(id, empresaId));
    }

    @CircuitBreaker(name = "produto-preco-base-admin", fallbackMethod = "fallbackAdminVoid2")
    public void removerPorProduto(Long produtoId) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.deleteByProdutoIdAndEmpresaId(produtoId, empresaId);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ProdutoPrecoBaseResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de preço base de produto temporariamente indisponível");
    }

    private Page<ProdutoPrecoBaseResponse> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de preço base de produto temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de preço base de produto temporariamente indisponível");
    }

    private void fallbackAdminVoid2(Long produtoId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de preço base de produto temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoPrecoBase buscarPreco(Long id, Long empresaId) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Preço base não encontrado: " + id));
    }

    private void validarDuplicidadeProduto(Long produtoId, Long empresaId) {
        if (repository.existsByProdutoIdAndEmpresaId(produtoId, empresaId))
            throw new IllegalArgumentException("Já existe preço base cadastrado para o produto informado.");
    }
}
