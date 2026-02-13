package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoResponse;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoResumoResponse;
import br.com.unicos.ms_produto.dto.categoria.CategoriaProdutoUpdateRequest;
import br.com.unicos.ms_produto.mapper.CategoriaProdutoMapper;
import br.com.unicos.ms_produto.model.CategoriaProduto;
import br.com.unicos.ms_produto.repository.CategoriaProdutoRepository;
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
public class CategoriaProdutoService extends BaseTenantService<CategoriaProduto, Long> {

    private final CategoriaProdutoRepository repository;
    private final CategoriaProdutoMapper mapper;

    public CategoriaProdutoService(
            CategoriaProdutoRepository repository,
            CategoriaProdutoMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "categoria-produto-admin", fallbackMethod = "fallbackAdmin")
    public CategoriaProdutoResponse criar(CategoriaProdutoCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarNomeDuplicado(request.nome(), empresaId);

        CategoriaProduto categoria = mapper.toEntity(request, empresaId);
        categoria.setAtivo(true);

        return mapper.toResponse(repository.save(categoria));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "categoria-produto-admin", fallbackMethod = "fallbackAdmin")
    public CategoriaProdutoResponse atualizar(Long id, CategoriaProdutoUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        CategoriaProduto categoria = buscarCategoria(id, empresaId);

        if (!categoria.getNome().equalsIgnoreCase(request.nome()))
            validarNomeDuplicado(request.nome(), empresaId);

        mapper.updateEntity(request, categoria);

        return mapper.toResponse(repository.save(categoria));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "categoria-produto-admin", fallbackMethod = "fallbackAdmin")
    public CategoriaProdutoResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarCategoria(id, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "categoria-produto-admin", fallbackMethod = "fallbackAdminPage")
    public Page<CategoriaProdutoResumoResponse> listar(Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findAllByEmpresaId(empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "categoria-produto-admin", fallbackMethod = "fallbackAdminPage")
    public Page<CategoriaProdutoResumoResponse> listarPorCategoriaPai(Long categoriaPaiId, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByCategoriaPaiIdAndEmpresaId(categoriaPaiId, empresaId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ============================================================
    // STATUS
    // ============================================================

    @CircuitBreaker(name = "categoria-produto-admin", fallbackMethod = "fallbackAdmin")
    public CategoriaProdutoResponse ativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        CategoriaProduto categoria = buscarCategoria(id, empresaId);
        categoria.setAtivo(true);

        return mapper.toResponse(repository.save(categoria));
    }

    @CircuitBreaker(name = "categoria-produto-admin", fallbackMethod = "fallbackAdmin")
    public CategoriaProdutoResponse inativar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        CategoriaProduto categoria = buscarCategoria(id, empresaId);
        categoria.setAtivo(false);

        return mapper.toResponse(repository.save(categoria));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "categoria-produto-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarCategoria(id, empresaId));
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private CategoriaProdutoResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de categorias de produto temporariamente indisponível");
    }

    private Page<CategoriaProdutoResumoResponse> fallbackAdminPage(Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de categorias de produto temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de categorias de produto temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private CategoriaProduto buscarCategoria(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria de produto não encontrada: " + id));
    }

    private void validarNomeDuplicado(String nome, Long empresaId) {
        if (repository.existsByNomeAndEmpresaId(nome, empresaId))
            throw new IllegalArgumentException("Já existe uma categoria de produto com o nome informado.");
    }
}
