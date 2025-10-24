package br.com.erp.ms_produtos.controller;

import br.com.erp.ms_produtos.dto.ProdutoRequest;
import br.com.erp.ms_produtos.dto.ProdutoResponse;
import br.com.erp.ms_produtos.model.Categoria;
import br.com.erp.ms_produtos.model.Marca;
import br.com.erp.ms_produtos.model.Produto;
import br.com.erp.ms_produtos.service.CategoriaService;
import br.com.erp.ms_produtos.service.MarcaService;
import br.com.erp.ms_produtos.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos produtos.
 * Fornece endpoints para operações CRUD e consultas específicas.
 *
 * @author Gustavo
 */
@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final MarcaService marcaService;
    private final ModelMapper modelMapper;

    /**
     * Cria um novo produto.
     *
     * @param request dados do produto
     * @return produto criado
     */
    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoRequest request) {
        Produto produto = toEntity(request);
        Produto salvo = produtoService.salvar(produto);
        return ResponseEntity.created(URI.create("/api/v1/produtos/" + salvo.getId()))
                .body(toResponse(salvo));
    }

    /**
     * Atualiza um produto existente.
     *
     * @param id      identificador do produto
     * @param request novos dados
     * @return produto atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody ProdutoRequest request) {
        Produto produto = toEntity(request);
        Produto atualizado = produtoService.atualizar(id, produto);
        return ResponseEntity.ok(toResponse(atualizado));
    }

    /**
     * Busca um produto pelo seu ID.
     *
     * @param id identificador do produto
     * @return produto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(produto -> ResponseEntity.ok(toResponse(produto)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os produtos cadastrados.
     *
     * @return lista de produtos
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        List<ProdutoResponse> lista = produtoService.listarTodos()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista produtos ativos.
     *
     * @return lista de produtos ativos
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<ProdutoResponse>> listarAtivos() {
        List<ProdutoResponse> lista = produtoService.listarAtivos()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista produtos por categoria.
     *
     * @param categoriaId ID da categoria
     * @return lista de produtos
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorCategoria(@PathVariable Long categoriaId) {
        List<ProdutoResponse> lista = produtoService.listarPorCategoria(categoriaId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(lista);
    }

    /**
     * Lista produtos por marca.
     *
     * @param marcaId ID da marca
     * @return lista de produtos
     */
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorMarca(@PathVariable Long marcaId) {
        List<ProdutoResponse> lista = produtoService.listarPorMarca(marcaId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(lista);
    }

    /**
     * Remove um produto pelo ID.
     *
     * @param id identificador do produto
     * @return 204 No Content em caso de sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Converte um DTO de requisição em entidade Produto.
     */
    private Produto toEntity(ProdutoRequest request) {
        Produto produto = modelMapper.map(request, Produto.class);

        Categoria categoria = categoriaService.buscarPorId(request.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));
        produto.setCategoria(categoria);

        if (request.marcaId() != null) {
            Marca marca = marcaService.buscarPorId(request.marcaId())
                    .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada."));
            produto.setMarca(marca);
        } else {
            produto.setMarca(null);
        }

        return produto;
    }

    /**
     * Converte uma entidade Produto em DTO de resposta.
     */
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