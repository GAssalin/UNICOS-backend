package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.produto.ProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.produto.ProdutoResponse;
import br.com.unicos.ms_produto.dto.produto.ProdutoResumoResponse;
import br.com.unicos.ms_produto.dto.produto.ProdutoUpdateRequest;
import br.com.unicos.ms_produto.mapper.ProdutoMapper;
import br.com.unicos.ms_produto.model.Produto;
import br.com.unicos.ms_produto.repository.ProdutoRepository;
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
public class ProdutoService extends BaseTenantService<Produto, Long> {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    public ProdutoService(
            ProdutoRepository repository,
            ProdutoMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "produto-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoResponse criar(ProdutoCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarCodigoDuplicado(request.codigo(), empresaId);

        Produto produto = mapper.toEntity(request, empresaId);
        produto.setEmpresaId(empresaId);
        produto.setAtivo(true);

        return mapper.toResponse(repository.save(produto), empresaId);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "produto-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoResponse atualizar(Long id, ProdutoUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        Produto produto = buscarProduto(id, empresaId);

        mapper.updateEntity(request, produto, empresaId);

        return mapper.toResponse(repository.save(produto), empresaId);
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarProduto(id, empresaId), empresaId);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoResponse buscarPorCodigo(String codigo) {
        Long empresaId = TenantContext.getEmpresaId();

        Produto produto = repository.findByCodigoAndEmpresaId(codigo, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado para o código: " + codigo));

        return mapper.toResponse(produto, empresaId);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ProdutoResumoResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();
        return repository.findAllByEmpresaId(empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ProdutoResumoResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();
        return repository.findByAtivoAndEmpresaId(ativo, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "produto-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ProdutoResumoResponse> pesquisarPorNome(String nome, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();
        return repository.findByNomeContainingIgnoreCaseAndEmpresaId(nome, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // STATUS
    // ============================================================

    @CircuitBreaker(name = "produto-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoResponse ativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        Produto produto = buscarProduto(id, empresaId);
        produto.setAtivo(true);

        return mapper.toResponse(repository.save(produto), empresaId);
    }

    @CircuitBreaker(name = "produto-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoResponse inativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        Produto produto = buscarProduto(id, empresaId);
        produto.setAtivo(false);

        return mapper.toResponse(repository.save(produto), empresaId);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "produto-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarProduto(id, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ProdutoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de produtos temporariamente indisponível");
    }

    private Page<ProdutoResumoResponse> fallbackAdminPage(Pageable pageable, Throwable ex) {
        ex.printStackTrace(); // ou log.error(...)
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de produtos temporariamente indisponível"
        );
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de produtos temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Produto buscarProduto(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));
    }

    private void validarCodigoDuplicado(String codigo, Long empresaId) {
        if (repository.existsByCodigoAndEmpresaId(codigo, empresaId))
            throw new IllegalArgumentException("Já existe um produto com o código (SKU) informado.");
    }
}
