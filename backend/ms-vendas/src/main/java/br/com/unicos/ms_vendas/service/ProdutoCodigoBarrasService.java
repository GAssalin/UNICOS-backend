package br.com.unicos.ms_vendas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_vendas.dto.produtocodigobarras.ProdutoCodigoBarrasCreateRequest;
import br.com.unicos.ms_vendas.dto.produtocodigobarras.ProdutoCodigoBarrasResponse;
import br.com.unicos.ms_vendas.dto.produtocodigobarras.ProdutoCodigoBarrasUpdateRequest;
import br.com.unicos.ms_vendas.mapper.ProdutoCodigoBarrasMapper;
import br.com.unicos.ms_vendas.model.ProdutoCodigoBarras;
import br.com.unicos.ms_vendas.repository.ProdutoCodigoBarrasRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ProdutoCodigoBarrasService extends BaseTenantService<ProdutoCodigoBarras, Long> {

    private final ProdutoCodigoBarrasRepository repository;
    private final ProdutoCodigoBarrasMapper mapper;

    public ProdutoCodigoBarrasService(
            ProdutoCodigoBarrasRepository repository,
            ProdutoCodigoBarrasMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "vendas-produto-codigo-barras-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoCodigoBarrasResponse criar(ProdutoCodigoBarrasCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        validarCodigoDuplicado(request.codigoBarras(), empresaId);

        if (request.principal())
            removerPrincipalAtual(request.produtoId(), empresaId);

        ProdutoCodigoBarras codigo = mapper.toEntity(request, empresaId);
        codigo.setAtivo(true);

        return mapper.toResponse(repository.save(codigo));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "vendas-produto-codigo-barras-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoCodigoBarrasResponse atualizar(Long id, ProdutoCodigoBarrasUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoCodigoBarras codigo = buscarCodigo(id, empresaId);

        if (!codigo.getCodigoBarras().equalsIgnoreCase(request.codigoBarras()))
            validarCodigoDuplicado(request.codigoBarras(), empresaId);

        if (request.principal())
            removerPrincipalAtual(codigo.getProdutoId(), empresaId);

        mapper.updateEntity(request, codigo);

        return mapper.toResponse(repository.save(codigo));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vendas-produto-codigo-barras-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoCodigoBarrasResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarCodigo(id, empresaId));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vendas-produto-codigo-barras-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoCodigoBarrasResponse buscarPorCodigoBarras(String codigoBarras) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoCodigoBarras codigo = repository.findByCodigoBarrasAndEmpresaId(codigoBarras, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Código de barras não encontrado: " + codigoBarras));

        return mapper.toResponse(codigo);
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vendas-produto-codigo-barras-admin", fallbackMethod = "fallbackAdminList")
    public List<ProdutoCodigoBarrasResponse> listarPorProduto(Long produtoId) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByProdutoIdAndEmpresaId(produtoId, empresaId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "vendas-produto-codigo-barras-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarCodigo(id, empresaId));
    }

    @CircuitBreaker(name = "vendas-produto-codigo-barras-admin", fallbackMethod = "fallbackAdminVoid2")
    public void removerPorProduto(Long produtoId) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.deleteByProdutoIdAndEmpresaId(produtoId, empresaId);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ProdutoCodigoBarrasResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de códigos de barras temporariamente indisponível");
    }

    private List<ProdutoCodigoBarrasResponse> fallbackAdminList(Long produtoId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de códigos de barras temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de códigos de barras temporariamente indisponível");
    }

    private void fallbackAdminVoid2(Long produtoId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de códigos de barras temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoCodigoBarras buscarCodigo(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Código de barras não encontrado: " + id));
    }

    private void validarCodigoDuplicado(String codigoBarras, Long empresaId) {
        if (repository.existsByCodigoBarrasAndEmpresaId(codigoBarras, empresaId))
            throw new IllegalArgumentException("Já existe um código de barras cadastrado com o valor informado.");
    }

    private void removerPrincipalAtual(Long produtoId, Long empresaId) {
        repository.findByProdutoIdAndPrincipalTrueAndEmpresaId(produtoId, empresaId)
                .ifPresent(c -> {
                    c.setPrincipal(false);
                    repository.save(c);
                });
    }
}
