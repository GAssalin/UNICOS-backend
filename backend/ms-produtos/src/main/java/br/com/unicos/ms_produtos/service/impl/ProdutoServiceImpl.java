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
import br.com.unicos.ms_produtos.service.HistoricoPrecoService;
import br.com.unicos.ms_produtos.service.ProdutoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface ProdutoService.
 *
 * <p>
 * Responsável pela orquestração das regras de negócio relacionadas
 * ao cadastro, atualização, consulta e manipulação do estado dos produtos.
 * </p>
 */
@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final HistoricoPrecoService historicoPrecoService;

    // 🔹 Mappers
    private final ProdutoVariacaoMapper produtoVariacaoMapper;
    private final ProdutoAtributoValorMapper produtoAtributoValorMapper;
    private final ImagemProdutoMapper imagemProdutoMapper;
    private final FornecedorProdutoMapper fornecedorProdutoMapper;
    private final CategoriaMapper categoriaMapper;
    private final MarcaMapper marcaMapper;

    public ProdutoServiceImpl(
            ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository,
            MarcaRepository marcaRepository,
            HistoricoPrecoService historicoPrecoService,
            ProdutoVariacaoMapper produtoVariacaoMapper,
            ProdutoAtributoValorMapper produtoAtributoValorMapper,
            ProdutoUnidadeMapper produtoUnidadeMapper,
            ImagemProdutoMapper imagemProdutoMapper,
            FornecedorProdutoMapper fornecedorProdutoMapper,
            CategoriaMapper categoriaMapper,
            MarcaMapper marcaMapper
    ) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.marcaRepository = marcaRepository;
        this.historicoPrecoService = historicoPrecoService;

        this.produtoVariacaoMapper = produtoVariacaoMapper;
        this.produtoAtributoValorMapper = produtoAtributoValorMapper;
        this.imagemProdutoMapper = imagemProdutoMapper;
        this.fornecedorProdutoMapper = fornecedorProdutoMapper;
        this.categoriaMapper = categoriaMapper;
        this.marcaMapper = marcaMapper;
    }

    // ============================================================
    // 🔹 CRUD PRINCIPAL
    // ============================================================

    @Override
    public ProdutoResponse salvar(ProdutoRequest request) {

        // Validar SKU
        if (request.dadosBasicos() != null && request.dadosBasicos().getSku() != null) {
            if (!verificarDisponibilidadeSku(request.dadosBasicos().getSku())) {
                throw new IllegalArgumentException("SKU já cadastrado.");
            }
        }

        Produto produto = Produto.builder()
                .dadosBasicos(request.dadosBasicos())
                .tributacao(request.tributacao())
                .estoqueConfig(request.estoqueConfig())
                .precoAtual(request.precoAtual())
                .ativo(true)
                .categoria(buscarCategoriaOuNull(request.categoriaId()))
                .marca(buscarMarcaOuNull(request.marcaId()))
                .build();

        Produto salvo = produtoRepository.save(produto);

        return toResponse(salvo);
    }

    @Override
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {

        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        // SKU alterado?
        if (request.dadosBasicos() != null) {
            String novoSku = request.dadosBasicos().getSku();
            if (novoSku != null && !novoSku.equals(existente.getDadosBasicos().getSku())) {
                produtoRepository.findByDadosBasicosSku(novoSku).ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        throw new IllegalArgumentException("SKU já está sendo usado por outro produto.");
                    }
                });
            }
        }

        BigDecimal precoAnterior = existente.getPrecoAtual().getPrecoVenda();

        // Atualizar dados universais
        if (request.dadosBasicos() != null) {
            existente.setDadosBasicos(request.dadosBasicos());
        }
        if (request.tributacao() != null) {
            existente.setTributacao(request.tributacao());
        }
        if (request.estoqueConfig() != null) {
            existente.setEstoqueConfig(request.estoqueConfig());
        }
        if (request.precoAtual() != null) {
            existente.setPrecoAtual(request.precoAtual());
        }

        existente.setCategoria(buscarCategoriaOuNull(request.categoriaId()));
        existente.setMarca(buscarMarcaOuNull(request.marcaId()));

        Produto atualizado = produtoRepository.save(existente);

        BigDecimal novoPreco = atualizado.getPrecoAtual().getPrecoVenda();

        if (precoAnterior != null && novoPreco != null && precoAnterior.compareTo(novoPreco) != 0) {
            historicoPrecoService.salvar(
                    atualizado.getId(),
                    new HistoricoPrecoRequest(precoAnterior, novoPreco, "Atualização de produto")
            );
        }

        return toResponse(atualizado);
    }

    @Override
    public Optional<ProdutoResponse> buscarPorId(Long id) {
        return produtoRepository.findById(id).map(this::toResponse);
    }

    // ============================================================
    // 🔹 LISTAGENS E CONSULTAS
    // ============================================================

    @Override
    public List<ProdutoResponse> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }

    @Override
    public Optional<ProdutoResponse> buscarPorSku(String sku) {
        return produtoRepository.findByDadosBasicosSku(sku)
                .map(this::toResponse);
    }

    @Override
    public List<ProdutoResponse> buscarPorNome(String nome) {
        return produtoRepository.findByDadosBasicosNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoResponse> listarPorCategoria(Long categoriaId) {
        return produtoRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoResponse> listarPorMarca(Long marcaId) {
        return produtoRepository.findByMarcaId(marcaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoResponse> listarAtivos() {
        return produtoRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoResponse> listarInativos() {
        return produtoRepository.findByAtivoFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProdutoResponse> listarPorFaixaDePreco(BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository.findByPrecoAtualPrecoVendaBetween(precoMin, precoMax)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ============================================================
    // 🔹 ALTERAÇÃO DE ESTADO
    // ============================================================

    @Override
    public ProdutoResponse ativarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + id));

        produto.setAtivo(true);
        return toResponse(produtoRepository.save(produto));
    }

    @Override
    public ProdutoResponse inativarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + id));

        produto.setAtivo(false);
        return toResponse(produtoRepository.save(produto));
    }


    // ============================================================
    // 🔹 PREÇO
    // ============================================================

    @Override
    public ProdutoResponse atualizarPreco(Long id, BigDecimal novoPreco) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + id));

        BigDecimal precoAnterior = produto.getPrecoAtual() != null
                ? produto.getPrecoAtual().getPrecoVenda()
                : null;

        if (produto.getPrecoAtual() == null) {
            produto.setPrecoAtual(new PrecoBase(
                    null,
                    novoPreco,
                    null,
                    null
            ));
        } else {
            produto.getPrecoAtual().setPrecoVenda(novoPreco);
        }

        Produto atualizado = produtoRepository.save(produto);

        if (precoAnterior != null && precoAnterior.compareTo(novoPreco) != 0) {
            historicoPrecoService.salvar(
                    atualizado.getId(),
                    new HistoricoPrecoRequest(precoAnterior, novoPreco, "Atualização de preço")
            );
        }

        return toResponse(atualizado);
    }

    // ============================================================
    // 🔹 VALIDAÇÃO DE SKU
    // ============================================================

    @Override
    public boolean verificarDisponibilidadeSku(String sku) {
        return produtoRepository.findByDadosBasicosSku(sku).isEmpty();
    }

    // ============================================================
    // 🔹 RESPONSE USANDO OS MAPPERS
    // ============================================================

    private ProdutoResponse toResponse(Produto produto) {

        return new ProdutoResponse(
                produto.getId(),
                produto.isAtivo(),
                produto.getDadosBasicos(),
                produto.getTributacao(),
                produto.getEstoqueConfig(),
                produto.getPrecoAtual(),

                // Categoria via mapper
                produto.getCategoria() != null
                        ? categoriaMapper.toResponse(produto.getCategoria())
                        : null,

                // Marca via mapper
                produto.getMarca() != null
                        ? marcaMapper.toResponse(produto.getMarca())
                        : null,

                // Imagens via mapper
                produto.getImagens() == null
                        ? List.of()
                        : produto.getImagens().stream()
                        .map(imagemProdutoMapper::toResponse)
                        .toList(),

                // Atributos via mapper
                produto.getAtributos() == null
                        ? List.of()
                        : produto.getAtributos().stream()
                        .map(a -> produtoAtributoValorMapper.toResponse(a, produto.getId()))
                        .toList(),

                // Fornecedores via mapper
                produto.getFornecedores() == null
                        ? List.of()
                        : produto.getFornecedores().stream()
                        .map(fornecedorProdutoMapper::toResponse)
                        .toList(),

                // Variações via mapper
                produto.getVariacoes() == null
                        ? List.of()
                        : produto.getVariacoes().stream()
                        .map(produtoVariacaoMapper::toResponse)
                        .toList()
        );
    }

    // ============================================================
    // 🧭 MÉTODOS AUXILIARES
    // ============================================================

    private Categoria buscarCategoriaOuNull(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com ID: " + categoriaId));
    }

    private Marca buscarMarcaOuNull(Long marcaId) {
        if (marcaId == null) {
            return null;
        }
        return marcaRepository.findById(marcaId)
                .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada com ID: " + marcaId));
    }

}
