package br.com.unicos.ms_produtos.service.impl;

import br.com.unicos.core.produto.model.ProdutoBase;
import br.com.unicos.ms_produtos.dto.produto.ProdutoRequest;
import br.com.unicos.ms_produtos.dto.produto.ProdutoResponse;
import br.com.unicos.ms_produtos.model.Categoria;
import br.com.unicos.ms_produtos.model.HistoricoPreco;
import br.com.unicos.ms_produtos.model.Marca;
import br.com.unicos.ms_produtos.model.Produto;
import br.com.unicos.ms_produtos.repository.CategoriaRepository;
import br.com.unicos.ms_produtos.repository.MarcaRepository;
import br.com.unicos.ms_produtos.repository.ProdutoRepository;
import br.com.unicos.ms_produtos.service.ProdutoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface ProdutoService.
 * Responsável pela lógica de negócio e orquestração das operações
 * de criação, atualização, exclusão e consulta de produtos.
 */
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

        // ============================================================
        // 🔹 1. Buscar o produto existente
        // ============================================================
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Produto não encontrado com o ID: " + id
                ));

        // ============================================================
        // 🔹 2. Validar e carregar Categoria (se houver)
        // ============================================================
        if (request.categoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.categoriaId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Categoria não encontrada com ID: " + request.categoriaId()
                    ));
            produto.setCategoria(categoria);
        }

        // ============================================================
        // 🔹 3. Validar e carregar Marca (se houver)
        // ============================================================
        if (request.marcaId() != null) {
            Marca marca = marcaRepository.findById(request.marcaId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Marca não encontrada com ID: " + request.marcaId()
                    ));
            produto.setMarca(marca);
        }

        // ============================================================
        // 🔹 4. Atualizar os dados básicos (ProdutoBase é imutável!)
        //      → precisa reconstruir o record
        // ============================================================
        ProdutoBase dadosAntigos = produto.getDadosBasicos();

        ProdutoBase novosDados = new ProdutoBase(
                dadosAntigos.id(), // mantém o ID interno do core
                request.nome(),
                request.sku(),
                request.descricao(),
                request.codigoBarras(),
                request.unidadeMedida(),
                request.tipoProduto(),
                request.origem(),
                request.controleEstoque(),
                request.armazenamento(),
                request.classificacao(),
                request.status()
        );

        produto.setDadosBasicos(novosDados);

        // ============================================================
        // 🔹 5. Atualizar demais atributos do Produto (não-core)
        // ============================================================
        produto.setDescricao(request.descricao());
        produto.setAtivo(request.ativo());
        produto.setPreco(request.preco());

        // Aqui você pode incluir mais atributos específicos do ms-produtos

        // ============================================================
        // 🔹 6. Salvar
        // ============================================================
        Produto atualizado = produtoRepository.save(produto);

        // ============================================================
        // 🔹 7. Retornar DTO
        // ============================================================
        return mapper.map(atualizado, ProdutoResponse.class);
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
        return produtoRepository.findBySku(sku)
                .map(this::toResponse);
    }

    @Override
    public List<ProdutoResponse> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome)
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
        return produtoRepository.findByPrecoBetween(precoMin, precoMax)
                .stream()
                .map(this::toResponse)
                .toList();
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

        BigDecimal precoAntigo = produto.getPrecoAtual().precoVenda();
        produto.setPreco(novoPreco);

        HistoricoPreco historico = HistoricoPreco.builder()
                .produto(produto)
                .precoAnterior(precoAntigo)
                .novoPreco(novoPreco)
                .build();

        produto.getHistoricoPrecos().add(historico);
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
                produto.getDadosBasicos().nome(),
                produto.getDadosBasicos().descricao(),
                produto.getPrecoAtual(),
                produto.getSku(),
                produto.getCategoria() != null ? produto.getCategoria().getNome() : null,
                produto.getMarca() != null ? produto.getMarca().getNome() : null,
                produto.isAtivo()
        );
    }
}