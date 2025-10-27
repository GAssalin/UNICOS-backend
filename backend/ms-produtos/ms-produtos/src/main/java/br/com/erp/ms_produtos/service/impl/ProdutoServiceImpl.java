package br.com.erp.ms_produtos.service.impl;

import br.com.erp.ms_produtos.dto.ProdutoRequest;
import br.com.erp.ms_produtos.dto.ProdutoResponse;
import br.com.erp.ms_produtos.model.*;
import br.com.erp.ms_produtos.repository.*;
import br.com.erp.ms_produtos.service.ProdutoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ModelMapper mapper;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository,
                              CategoriaRepository categoriaRepository,
                              MarcaRepository marcaRepository,
                              ModelMapper mapper) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.marcaRepository = marcaRepository;
        this.mapper = mapper;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    @Override
    public ProdutoResponse salvar(ProdutoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));
        Marca marca = null;
        if (request.marcaId() != null) {
            marca = marcaRepository.findById(request.marcaId())
                    .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada."));
        }

        Produto produto = mapper.map(request, Produto.class);
        produto.setCategoria(categoria);
        produto.setMarca(marca);

        Produto salvo = produtoRepository.save(produto);
        return toResponse(salvo);
    }

    @Override
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + id));

        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));
        Marca marca = request.marcaId() != null
                ? marcaRepository.findById(request.marcaId()).orElse(null)
                : null;

        existente.setNome(request.nome());
        existente.setDescricao(request.descricao());
        existente.setPreco(request.preco());
        existente.setCategoria(categoria);
        existente.setMarca(marca);
        existente.setSku(request.sku());
        existente.setAtivo(request.ativo());

        Produto atualizado = produtoRepository.save(existente);
        return toResponse(atualizado);
    }

    @Override
    public Optional<ProdutoResponse> buscarPorId(Long id) {
        return produtoRepository.findById(id).map(this::toResponse);
    }

    @Override
    public List<ProdutoResponse> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }

    // ==================================
    // 🔹 BUSCAS
    // ==================================

    @Override
    public Optional<ProdutoResponse> buscarPorSku(String sku) {
        return produtoRepository.findBySku(sku).map(this::toResponse);
    }

    @Override
    public List<ProdutoResponse> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<ProdutoResponse> listarPorCategoria(Long categoriaId) {
        return produtoRepository.findByCategoriaId(categoriaId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<ProdutoResponse> listarPorMarca(Long marcaId) {
        return produtoRepository.findByMarcaId(marcaId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<ProdutoResponse> listarAtivos() {
        return produtoRepository.findByAtivoTrue()
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<ProdutoResponse> listarInativos() {
        return produtoRepository.findByAtivoFalse()
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<ProdutoResponse> listarPorFaixaDePreco(BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository.findByPrecoBetween(precoMin, precoMax)
                .stream().map(this::toResponse).toList();
    }

    // ==================================
    // 💼 NEGÓCIO
    // ==================================

    @Override
    public ProdutoResponse ativarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        produto.setAtivo(true);
        return toResponse(produtoRepository.save(produto));
    }

    @Override
    public ProdutoResponse inativarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        produto.setAtivo(false);
        return toResponse(produtoRepository.save(produto));
    }

    @Override
    public ProdutoResponse atualizarPreco(Long id, BigDecimal novoPreco) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        BigDecimal precoAntigo = produto.getPreco();
        produto.setPreco(novoPreco);

        HistoricoPreco historico = HistoricoPreco.builder()
                .produto(produto)
                .precoAnterior(precoAntigo)
                .novoPreco(novoPreco)
                .dataAlteracao(LocalDateTime.now())
                .build();

        produto.getHistoricosPreco().add(historico);
        return toResponse(produtoRepository.save(produto));
    }

    @Override
    public boolean verificarDisponibilidadeSku(String sku) {
        return produtoRepository.findBySku(sku).isEmpty();
    }

    // ==================================
    // 🧭 MAPEAMENTO AUXILIAR
    // ==================================

    private ProdutoResponse toResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getSku(),
                produto.getCategoria() != null ? produto.getCategoria().getNome() : null,
                produto.getMarca() != null ? produto.getMarca().getNome() : null,
                produto.getAtivo()
        );
    }
}