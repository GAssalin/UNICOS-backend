package br.com.unicos.ms_vendas.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_vendas.dto.produtoimagem.ProdutoImagemCreateRequest;
import br.com.unicos.ms_vendas.dto.produtoimagem.ProdutoImagemResponse;
import br.com.unicos.ms_vendas.dto.produtoimagem.ProdutoImagemUpdateRequest;
import br.com.unicos.ms_vendas.mapper.ProdutoImagemMapper;
import br.com.unicos.ms_vendas.model.ProdutoImagem;
import br.com.unicos.ms_vendas.repository.ProdutoImagemRepository;
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
public class ProdutoImagemService extends BaseTenantService<ProdutoImagem, Long> {

    private final ProdutoImagemRepository repository;
    private final ProdutoImagemMapper mapper;

    public ProdutoImagemService(
            ProdutoImagemRepository repository,
            ProdutoImagemMapper mapper
    ) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @CircuitBreaker(name = "vendas-produto-imagem-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoImagemResponse criar(ProdutoImagemCreateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        if (request.principal())
            removerPrincipalAtual(request.produtoId(), empresaId);

        ProdutoImagem imagem = mapper.toEntity(request, empresaId);
        imagem.setAtivo(true);

        return mapper.toResponse(repository.save(imagem));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @CircuitBreaker(name = "vendas-produto-imagem-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoImagemResponse atualizar(Long id, ProdutoImagemUpdateRequest request) {
        Long empresaId = TenantContext.getEmpresaId();

        ProdutoImagem imagem = buscarImagem(id, empresaId);

        if (request.principal())
            removerPrincipalAtual(imagem.getProdutoId(), empresaId);

        mapper.updateEntity(request, imagem);

        return mapper.toResponse(repository.save(imagem));
    }

    // ============================================================
    // GET
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vendas-produto-imagem-admin", fallbackMethod = "fallbackAdmin")
    public ProdutoImagemResponse buscarPorId(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        return mapper.toResponse(buscarImagem(id, empresaId));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "vendas-produto-imagem-admin", fallbackMethod = "fallbackAdminPage")
    public Page<ProdutoImagemResponse> listarPorProduto(Long produtoId, Pageable pageable) {
        Long empresaId = TenantContext.getEmpresaId();

        return repository.findByProdutoIdAndEmpresaId(produtoId, empresaId, pageable)
                .map(mapper::toResponse);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @CircuitBreaker(name = "vendas-produto-imagem-admin", fallbackMethod = "fallbackAdminVoid")
    public void remover(Long id) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.delete(buscarImagem(id, empresaId));
    }

    @CircuitBreaker(name = "vendas-produto-imagem-admin", fallbackMethod = "fallbackAdminVoid2")
    public void removerPorProduto(Long produtoId) {
        Long empresaId = TenantContext.getEmpresaId();
        repository.deleteByProdutoIdAndEmpresaId(produtoId, empresaId);
    }

    // ============================================================
    // FALLBACKS
    // ============================================================

    private ProdutoImagemResponse fallbackAdmin(Object req, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de imagens de produto temporariamente indisponível");
    }

    private Page<ProdutoImagemResponse> fallbackAdminPage(Long produtoId, Pageable pageable, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de imagens de produto temporariamente indisponível");
    }

    private void fallbackAdminVoid(Long id, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de imagens de produto temporariamente indisponível");
    }

    private void fallbackAdminVoid2(Long produtoId, Throwable ex) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de imagens de produto temporariamente indisponível");
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoImagem buscarImagem(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Imagem de produto não encontrada: " + id));
    }

    private void removerPrincipalAtual(Long produtoId, Long empresaId) {
        repository.findByProdutoIdAndPrincipalTrueAndEmpresaId(produtoId, empresaId)
                .ifPresent(img -> {
                    img.setPrincipal(false);
                    repository.save(img);
                });
    }
}
