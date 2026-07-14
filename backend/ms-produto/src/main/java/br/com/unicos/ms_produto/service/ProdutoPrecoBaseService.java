package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.produtoprecobase.ProdutoPrecoBaseCreateRequest;
import br.com.unicos.ms_produto.dto.produtoprecobase.ProdutoPrecoBaseResponse;
import br.com.unicos.ms_produto.dto.produtoprecobase.ProdutoPrecoBaseUpdateRequest;
import br.com.unicos.ms_produto.mapper.ProdutoPrecoBaseMapper;
import br.com.unicos.ms_produto.model.ProdutoPrecoBase;
import br.com.unicos.ms_produto.repository.ProdutoPrecoBaseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdutoPrecoBaseService extends BaseTenantService<ProdutoPrecoBase, Long> {

    private final ProdutoPrecoBaseRepository repository;
    private final ProdutoPrecoBaseMapper mapper;

    public ProdutoPrecoBaseService(ProdutoPrecoBaseRepository repository, ProdutoPrecoBaseMapper mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProdutoPrecoBaseResponse criar(ProdutoPrecoBaseCreateRequest request) {
        validarDuplicidadeProduto(request.produtoId());

        ProdutoPrecoBase preco = mapper.toEntity(request, TenantContext.getEmpresaId());
        preco.setAtivo(true);

        return mapper.toResponse(repository.save(preco));
    }

    public ProdutoPrecoBaseResponse atualizar(Long id, ProdutoPrecoBaseUpdateRequest request) {
        ProdutoPrecoBase preco = buscarPreco(id);

        mapper.updateEntity(request, preco);

        return mapper.toResponse(repository.save(preco));
    }

    @Transactional(readOnly = true)
    public ProdutoPrecoBaseResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarPreco(id));
    }

    @Transactional(readOnly = true)
    public ProdutoPrecoBaseResponse buscarPorProduto(Long produtoId) {
        ProdutoPrecoBase preco = repository.findByProdutoIdAndEmpresaId(produtoId, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Preço base não encontrado para o produto: " + produtoId));

        return mapper.toResponse(preco);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoPrecoBaseResponse> listar(Pageable pageable) {
        return repository.findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoPrecoBaseResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        return repository.findByAtivoAndEmpresaId(ativo, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResponse);
    }

    public void remover(Long id) {
        repository.delete(buscarPreco(id));
    }

    public void removerPorProduto(Long produtoId) {
        repository.deleteByProdutoIdAndEmpresaId(produtoId, TenantContext.getEmpresaId());
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private ProdutoPrecoBase buscarPreco(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Preço base não encontrado: " + id));
    }

    private void validarDuplicidadeProduto(Long produtoId) {
        if (repository.existsByProdutoIdAndEmpresaId(produtoId, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe preço base cadastrado para o produto informado.");
    }
}
