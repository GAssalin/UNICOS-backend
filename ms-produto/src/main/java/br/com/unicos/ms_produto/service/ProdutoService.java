package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.produto.ProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.produto.ProdutoListDTO;
import br.com.unicos.ms_produto.dto.produto.ProdutoResponse;
import br.com.unicos.ms_produto.dto.produto.ProdutoUpdateRequest;
import br.com.unicos.ms_produto.mapper.ProdutoMapper;
import br.com.unicos.ms_produto.model.CategoriaProduto;
import br.com.unicos.ms_produto.model.Produto;
import br.com.unicos.ms_produto.repository.CategoriaProdutoRepository;
import br.com.unicos.ms_produto.repository.MarcaProdutoRepository;
import br.com.unicos.ms_produto.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdutoService extends BaseTenantService<Produto, Long> {

    private final CategoriaProdutoRepository categoriaProdutoRepository;
    private final MarcaProdutoRepository marcaProdutoRepository;
    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    public ProdutoService(CategoriaProdutoRepository categoriaProdutoRepository, MarcaProdutoRepository marcaProdutoRepository, ProdutoRepository repository, ProdutoMapper mapper) {
        super(repository);
        this.categoriaProdutoRepository = categoriaProdutoRepository;
        this.marcaProdutoRepository = marcaProdutoRepository;
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProdutoResponse criar(ProdutoCreateRequest request) {
        validarCodigoDuplicado(request.codigo());
        CategoriaProduto categoria = buscarCategoria(request.categoriaId());
        validarMarca(request.marcaId());

        Produto produto = mapper.toEntity(request, TenantContext.getEmpresaId());
        produto.setAtivo(true);

        return mapper.toResponse(repository.save(produto), TenantContext.getEmpresaId(), categoria);
    }

    public ProdutoResponse atualizar(Long id, ProdutoUpdateRequest request) {
        Produto produto = buscarProduto(id);
        CategoriaProduto categoria = buscarCategoria(request.categoriaId());
        validarMarca(request.marcaId());

        mapper.updateEntity(produto, request, TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(produto), TenantContext.getEmpresaId(), categoria);
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        return toResponse(buscarProduto(id));
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorCodigo(String codigo) {
        Produto produto = repository.findByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado para o código: " + codigo));

        return toResponse(produto);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoListDTO> listar(Pageable pageable) {
        return repository.findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toListDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoListDTO> listarPorAtivo(Boolean ativo, Pageable pageable) {
        return repository.findByAtivoAndEmpresaId(ativo, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toListDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoListDTO> pesquisarPorNome(String nome, Pageable pageable) {
        return repository.findByNomeContainingIgnoreCaseAndEmpresaId(nome, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toListDTO);
    }

    public ProdutoResponse ativar(Long id) {
        Produto produto = buscarProduto(id);
        produto.setAtivo(true);

        return toResponse(repository.save(produto));
    }

    public ProdutoResponse inativar(Long id) {
        Produto produto = buscarProduto(id);
        produto.setAtivo(false);

        return toResponse(repository.save(produto));
    }

    public void remover(Long id) {
        repository.delete(buscarProduto(id));
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Produto buscarProduto(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));
    }

    private ProdutoResponse toResponse(Produto produto) {
        CategoriaProduto categoria = produto.getCategoriaId() == null
                ? null
                : categoriaProdutoRepository.findByIdAndEmpresaId(produto.getCategoriaId(), TenantContext.getEmpresaId()).orElse(null);

        return mapper.toResponse(produto, TenantContext.getEmpresaId(), categoria);
    }

    /**
     * A categoria é opcional; quando informada, precisa pertencer à empresa.
     */
    private CategoriaProduto buscarCategoria(Long categoriaId) {
        if (categoriaId == null)
            return null;

        return categoriaProdutoRepository.findByIdAndEmpresaId(categoriaId, TenantContext.getEmpresaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria de produto não encontrada para a empresa: " + categoriaId));
    }

    private void validarMarca(Long marcaId) {
        if (marcaId != null && !marcaProdutoRepository.existsByIdAndEmpresaId(marcaId, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Marca de produto não encontrada para a empresa: " + marcaId);
    }

    private void validarCodigoDuplicado(String codigo) {
        if (repository.existsByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um produto com o código (SKU) informado.");
    }
}
