package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.core.produto.model.PrecoBase;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoRequest;
import br.com.unicos.ms_produtos.dto.produto.ProdutoRequest;
import br.com.unicos.ms_produtos.dto.produto.ProdutoResponse;
import br.com.unicos.ms_produtos.mapper.*;
import br.com.unicos.ms_produtos.model.Categoria;
import br.com.unicos.ms_produtos.model.Marca;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.repository.CategoriaRepository;
import br.com.unicos.ms_produtos.repository.MarcaRepository;
import br.com.unicos.ms_produtos.repository.ProdutoRepository;
import br.com.unicos.ms_produtos.service.interfaces.HistoricoPrecoService;
import br.com.unicos.ms_produtos.service.interfaces.ProdutoService;
import br.com.unicos.ms_produtos.tenant.TenantContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link ProdutoService}.
 *
 * <p>
 * Responsável por orquestrar todas as regras de negócio relacionadas
 * ao cadastro, atualização, consulta, ativação/inativação e precificação
 * de produtos, garantindo isolamento total entre empresas (multi-tenant).
 * </p>
 */
@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final HistoricoPrecoService historicoPrecoService;

    private final ProdutoMapper produtoMapper;

    public ProdutoServiceImpl(
            ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository,
            MarcaRepository marcaRepository,
            HistoricoPrecoService historicoPrecoService,
            ProdutoMapper produtoMapper,
            ProdutoVariacaoMapper produtoVariacaoMapper,
            ProdutoAtributoValorMapper produtoAtributoValorMapper,
            ImagemProdutoMapper imagemProdutoMapper,
            FornecedorProdutoMapper fornecedorProdutoMapper,
            CategoriaMapper categoriaMapper,
            MarcaMapper marcaMapper
    ) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.marcaRepository = marcaRepository;
        this.historicoPrecoService = historicoPrecoService;
        this.produtoMapper = produtoMapper;
    }

    // ============================================================
    // CRUD
    // ============================================================

    @Override
    public ProdutoResponse salvar(ProdutoRequest request) {

        Long empresaId = TenantContext.getEmpresaId();

        if (request.dadosBasicos() != null
                && request.dadosBasicos().getSku() != null
                && produtoRepository.existsByEmpresaIdAndDadosBasicosSku(
                empresaId,
                request.dadosBasicos().getSku())) {
            throw new IllegalArgumentException("SKU já cadastrado para esta empresa.");
        }

        Produto produto = Produto.builder()
                .empresaId(empresaId)
                .dadosBasicos(request.dadosBasicos())
                .tributacao(request.tributacao())
                .precoAtual(request.precoAtual())
                .ativo(true)
                .categoria(buscarCategoriaOuNull(empresaId, request.categoriaId()))
                .marca(buscarMarcaOuNull(empresaId, request.marcaId()))
                .build();

        return produtoMapper.toResponse(produtoRepository.save(produto));
    }

    @Override
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {

        Long empresaId = TenantContext.getEmpresaId();

        Produto existente = produtoRepository.findByEmpresaIdAndId(empresaId, id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        String novoSku = request.dadosBasicos() != null
                ? request.dadosBasicos().getSku()
                : null;

        if (novoSku != null
                && !novoSku.equals(existente.getDadosBasicos().getSku())
                && produtoRepository.existsByEmpresaIdAndDadosBasicosSku(empresaId, novoSku)) {
            throw new IllegalArgumentException("SKU já utilizado por outro produto.");
        }

        BigDecimal precoAnterior = existente.getPrecoAtual() != null
                ? existente.getPrecoAtual().getPrecoVenda()
                : null;

        if (request.dadosBasicos() != null)
            existente.setDadosBasicos(request.dadosBasicos());

        if (request.tributacao() != null)
            existente.setTributacao(request.tributacao());

        if (request.precoAtual() != null)
            existente.setPrecoAtual(request.precoAtual());

        existente.setCategoria(buscarCategoriaOuNull(empresaId, request.categoriaId()));
        existente.setMarca(buscarMarcaOuNull(empresaId, request.marcaId()));

        Produto atualizado = produtoRepository.save(existente);

        BigDecimal novoPreco = atualizado.getPrecoAtual().getPrecoVenda();

        if (precoAnterior != null && precoAnterior.compareTo(novoPreco) != 0) {
            historicoPrecoService.salvar(
                    atualizado.getId(),
                    new HistoricoPrecoRequest(
                            precoAnterior,
                            novoPreco,
                            "Atualização de produto"
                    )
            );
        }

        return produtoMapper.toResponse(atualizado);
    }

    @Override
    public Optional<ProdutoResponse> buscarPorId(Long id) {
        return produtoRepository
                .findByEmpresaIdAndId(TenantContext.getEmpresaId(), id)
                .map(produtoMapper::toResponse);
    }

    @Override
    public List<ProdutoResponse> listarTodos() {
        return produtoRepository
                .findByEmpresaId(TenantContext.getEmpresaId())
                .stream()
                .map(produtoMapper::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        Long empresaId = TenantContext.getEmpresaId();

        if (!produtoRepository.existsByEmpresaIdAndId(empresaId, id)) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        produtoRepository.deleteById(id);
    }

    // ============================================================
    // CONSULTAS
    // ============================================================

    @Override
    public Optional<ProdutoResponse> buscarPorSku(String sku) {
        return produtoRepository
                .findByEmpresaIdAndDadosBasicosSku(TenantContext.getEmpresaId(), sku)
                .map(produtoMapper::toResponse);
    }

    @Override
    public List<ProdutoResponse> buscarPorNome(String nome) {
        return produtoRepository
                .findByEmpresaIdAndDadosBasicosNomeContainingIgnoreCase(
                        TenantContext.getEmpresaId(), nome)
                .stream()
                .map(produtoMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoResponse> listarPorCategoria(Long categoriaId) {
        return produtoRepository
                .findByEmpresaIdAndCategoriaId(TenantContext.getEmpresaId(), categoriaId)
                .stream()
                .map(produtoMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoResponse> listarPorMarca(Long marcaId) {
        return produtoRepository
                .findByEmpresaIdAndMarcaId(TenantContext.getEmpresaId(), marcaId)
                .stream()
                .map(produtoMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoResponse> listarAtivos() {
        return produtoRepository
                .findByEmpresaIdAndAtivoTrue(TenantContext.getEmpresaId())
                .stream()
                .map(produtoMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoResponse> listarInativos() {
        return produtoRepository
                .findByEmpresaIdAndAtivoFalse(TenantContext.getEmpresaId())
                .stream()
                .map(produtoMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoResponse> listarPorFaixaDePreco(BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository
                .findByEmpresaIdAndPrecoAtualPrecoVendaBetween(
                        TenantContext.getEmpresaId(), precoMin, precoMax)
                .stream()
                .map(produtoMapper::toResponse)
                .toList();
    }

    // ============================================================
    // ESTADO E PREÇO
    // ============================================================

    @Override
    public ProdutoResponse ativarProduto(Long id) {
        Produto produto = produtoRepository
                .findByEmpresaIdAndId(TenantContext.getEmpresaId(), id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        produto.setAtivo(true);
        return produtoMapper.toResponse(produtoRepository.save(produto));
    }

    @Override
    public ProdutoResponse inativarProduto(Long id) {
        Produto produto = produtoRepository
                .findByEmpresaIdAndId(TenantContext.getEmpresaId(), id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        produto.setAtivo(false);
        return produtoMapper.toResponse(produtoRepository.save(produto));
    }

    @Override
    public ProdutoResponse atualizarPreco(Long id, BigDecimal novoPreco) {

        Long empresaId = TenantContext.getEmpresaId();

        Produto produto = produtoRepository.findByEmpresaIdAndId(empresaId, id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        BigDecimal precoAnterior = produto.getPrecoAtual() != null
                ? produto.getPrecoAtual().getPrecoVenda()
                : null;

        if (produto.getPrecoAtual() == null)
            produto.setPrecoAtual(new PrecoBase(null, novoPreco, null, null));
        else
            produto.getPrecoAtual().setPrecoVenda(novoPreco);

        Produto atualizado = produtoRepository.save(produto);

        if (precoAnterior != null && precoAnterior.compareTo(novoPreco) != 0) {
            historicoPrecoService.salvar(
                    atualizado.getId(),
                    new HistoricoPrecoRequest(
                            precoAnterior,
                            novoPreco,
                            "Atualização manual de preço"
                    )
            );
        }

        return produtoMapper.toResponse(atualizado);
    }

    @Override
    public boolean verificarDisponibilidadeSku(String sku) {
        return !produtoRepository.existsByEmpresaIdAndDadosBasicosSku(
                TenantContext.getEmpresaId(),
                sku
        );
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private Categoria buscarCategoriaOuNull(Long empresaId, Long categoriaId) {
        if (categoriaId == null) return null;

        return categoriaRepository.findByEmpresaIdAndId(empresaId, categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));
    }

    private Marca buscarMarcaOuNull(Long empresaId, Long marcaId) {
        if (marcaId == null) return null;

        return marcaRepository.findByEmpresaIdAndId(empresaId, marcaId)
                .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada."));
    }
}
