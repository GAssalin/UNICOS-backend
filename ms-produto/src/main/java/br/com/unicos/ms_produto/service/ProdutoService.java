package br.com.unicos.ms_produto.service;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.core.tenant.service.BaseTenantService;
import br.com.unicos.ms_produto.dto.produto.ProdutoCreateRequest;
import br.com.unicos.ms_produto.dto.produto.ProdutoResponse;
import br.com.unicos.ms_produto.dto.produto.ProdutoResumoResponse;
import br.com.unicos.ms_produto.dto.produto.ProdutoUpdateRequest;
import br.com.unicos.ms_produto.mapper.ProdutoMapper;
import br.com.unicos.ms_produto.model.Produto;
import br.com.unicos.ms_produto.repository.CategoriaProdutoRepository;
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
    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    public ProdutoService(CategoriaProdutoRepository categoriaProdutoRepository, ProdutoRepository repository, ProdutoMapper mapper) {
        super(repository);
        this.categoriaProdutoRepository = categoriaProdutoRepository;
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProdutoResponse criar(ProdutoCreateRequest request) {
        validarCodigoDuplicado(request.codigo());

        Produto produto = mapper.toEntity(request, TenantContext.getEmpresaId());
        produto.setEmpresaId(TenantContext.getEmpresaId());
        produto.setAtivo(true);

        return mapper.toResponse(repository.save(produto), TenantContext.getEmpresaId(), categoriaProdutoRepository.getReferenceById(request.categoriaId()));
    }

    public ProdutoResponse atualizar(Long id, ProdutoUpdateRequest request) {
        Produto produto = buscarProduto(id);

        mapper.updateEntity(request, produto, TenantContext.getEmpresaId());

        return mapper.toResponse(repository.save(produto), TenantContext.getEmpresaId(), categoriaProdutoRepository.getReferenceById(request.categoriaId()));
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = buscarProduto(id);
        return mapper.toResponse(produto, TenantContext.getEmpresaId(), categoriaProdutoRepository.getReferenceById(produto.getCategoriaId()));
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorCodigo(String codigo) {
        Produto produto = repository.findByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado para o código: " + codigo));

        return mapper.toResponse(produto, TenantContext.getEmpresaId(), categoriaProdutoRepository.getReferenceById(produto.getCategoriaId()));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResumoResponse> listar(Pageable pageable) {
        return repository.findAllByEmpresaId(TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResumoResponse> listarPorAtivo(Boolean ativo, Pageable pageable) {
        return repository.findByAtivoAndEmpresaId(ativo, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResumoResponse> pesquisarPorNome(String nome, Pageable pageable) {
        return repository.findByNomeContainingIgnoreCaseAndEmpresaId(nome, TenantContext.getEmpresaId(), pageable)
                .map(mapper::toResumoResponse);
    }

    public ProdutoResponse ativar(Long id) {
        Produto produto = buscarProduto(id);
        produto.setAtivo(true);

        return mapper.toResponse(repository.save(produto), TenantContext.getEmpresaId(), categoriaProdutoRepository.getReferenceById(produto.getCategoriaId()));
    }

    public ProdutoResponse inativar(Long id) {
        Produto produto = buscarProduto(id);
        produto.setAtivo(false);

        return mapper.toResponse(repository.save(produto), TenantContext.getEmpresaId(), categoriaProdutoRepository.getReferenceById(produto.getCategoriaId()));
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

    private void validarCodigoDuplicado(String codigo) {
        if (repository.existsByCodigoAndEmpresaId(codigo, TenantContext.getEmpresaId()))
            throw new IllegalArgumentException("Já existe um produto com o código (SKU) informado.");
    }
}
