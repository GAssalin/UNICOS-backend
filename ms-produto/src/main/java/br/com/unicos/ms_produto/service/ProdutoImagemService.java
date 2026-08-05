package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.produtoimagem.ProdutoImagemCreateRequest;
import br.com.unicos.ms_produto.dto.produtoimagem.ProdutoImagemResponse;
import br.com.unicos.ms_produto.dto.produtoimagem.ProdutoImagemUpdateRequest;
import br.com.unicos.ms_produto.mapper.ProdutoImagemMapper;
import br.com.unicos.ms_produto.model.ProdutoImagem;
import br.com.unicos.ms_produto.repository.ProdutoImagemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdutoImagemService extends BaseTenantService<ProdutoImagem, Long> {

    private final ProdutoImagemRepository repository;
    private final ProdutoImagemMapper mapper;

    public ProdutoImagemService(ProdutoImagemRepository repository, ProdutoImagemMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProdutoImagemResponse criar(ProdutoImagemCreateRequest request) {
        if (request.principal())
            removerPrincipalAtual(request.produtoId());

        ProdutoImagem imagem = mapper.toEntity(request, TenantContext.getEmpresaId());
        imagem.setAtivo(true);

        return mapper.toResponse(repository.save(imagem));
    }

    public ProdutoImagemResponse atualizar(Long id, ProdutoImagemUpdateRequest request) {
        ProdutoImagem imagem = buscarImagem(id);

        if (request.principal())
            removerPrincipalAtual(imagem.getProdutoId());

        mapper.updateEntity(request, imagem);

        return mapper.toResponse(repository.save(imagem));
    }

    @Transactional(readOnly = true)
    public ProdutoImagemResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarImagem(id));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoImagemResponse> listarPorProduto(Long produtoId, Pageable pageable) {
        return repository.findByProdutoIdAndEmpresaId(produtoId, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResponse);
    }

    public void remover(Long id) {
        repository.delete(buscarImagem(id));
    }

    public void removerPorProduto(Long produtoId) {
        repository.deleteByProdutoIdAndEmpresaId(produtoId, TenantContext.getEmpresaId());
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoImagem buscarImagem(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Imagem de produto não encontrada: " + id));
    }

    private void removerPrincipalAtual(Long produtoId) {
        repository.findByProdutoIdAndPrincipalTrueAndEmpresaId(produtoId, TenantContext.getEmpresaId())
                .ifPresent(img -> {
                    img.setPrincipal(false);
                    repository.save(img);
                });
    }
}
