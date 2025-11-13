package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.produto.ProdutoRequest;
import br.com.unicos.ms_produtos.dto.produto.ProdutoResponse;
import br.com.unicos.ms_produtos.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos produtos.
 *
 * Fornece endpoints para operações de CRUD e consultas específicas.
 */
@RestController
@RequestMapping("/v1/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cadastra um novo produto.
     *
     * @param request Dados do produto a ser criado.
     * @return ProdutoResponse com os dados do produto criado.
     */
    @PostMapping
    public ResponseEntity<ProdutoResponse> criarProduto(@Valid @RequestBody ProdutoRequest request) {
        ProdutoResponse response = produtoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de um produto existente.
     *
     * @param id      Identificador do produto.
     * @param request Dados atualizados do produto.
     * @return ProdutoResponse atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizarProduto(@PathVariable Long id,
                                                            @Valid @RequestBody ProdutoRequest request) {
        ProdutoResponse response = produtoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um produto pelo seu ID.
     *
     * @param id Identificador do produto.
     * @return ProdutoResponse encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os produtos.
     *
     * @return Lista de ProdutoResponse.
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        List<ProdutoResponse> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    /**
     * Remove um produto pelo seu ID.
     *
     * @param id Identificador do produto.
     * @return Resposta 204 (sem conteúdo).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Busca produto pelo SKU.
     *
     * @param sku Código SKU.
     * @return ProdutoResponse correspondente.
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProdutoResponse> buscarPorSku(@PathVariable String sku) {
        return produtoService.buscarPorSku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca produtos por nome (parcial ou completo).
     *
     * @param nome Nome do produto.
     * @return Lista de produtos que correspondem ao nome.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponse>> buscarPorNome(@RequestParam String nome) {
        List<ProdutoResponse> produtos = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(produtos);
    }

    /**
     * Lista produtos de uma determinada categoria.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de produtos dessa categoria.
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorCategoria(@PathVariable Long categoriaId) {
        List<ProdutoResponse> produtos = produtoService.listarPorCategoria(categoriaId);
        return ResponseEntity.ok(produtos);
    }

    /**
     * Lista produtos de uma determinada marca.
     *
     * @param marcaId ID da marca.
     * @return Lista de produtos dessa marca.
     */
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorMarca(@PathVariable Long marcaId) {
        List<ProdutoResponse> produtos = produtoService.listarPorMarca(marcaId);
        return ResponseEntity.ok(produtos);
    }

    /**
     * Lista apenas os produtos ativos.
     *
     * @return Lista de produtos ativos.
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<ProdutoResponse>> listarAtivos() {
        return ResponseEntity.ok(produtoService.listarAtivos());
    }

    /**
     * Lista apenas os produtos inativos.
     *
     * @return Lista de produtos inativos.
     */
    @GetMapping("/inativos")
    public ResponseEntity<List<ProdutoResponse>> listarInativos() {
        return ResponseEntity.ok(produtoService.listarInativos());
    }

    /**
     * Lista produtos dentro de uma faixa de preço.
     *
     * @param precoMin Preço mínimo.
     * @param precoMax Preço máximo.
     * @return Lista de produtos dentro do intervalo.
     */
    @GetMapping("/faixa-preco")
    public ResponseEntity<List<ProdutoResponse>> listarPorFaixaDePreco(@RequestParam BigDecimal precoMin,
                                                                       @RequestParam BigDecimal precoMax) {
        return ResponseEntity.ok(produtoService.listarPorFaixaDePreco(precoMin, precoMax));
    }

    // ==================================
    // 💼 OPERAÇÕES DE NEGÓCIO
    // ==================================

    /**
     * Ativa um produto.
     *
     * @param id ID do produto.
     * @return ProdutoResponse com o status atualizado.
     */
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ProdutoResponse> ativarProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.ativarProduto(id));
    }

    /**
     * Inativa um produto.
     *
     * @param id ID do produto.
     * @return ProdutoResponse com o status atualizado.
     */
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<ProdutoResponse> inativarProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.inativarProduto(id));
    }

    /**
     * Atualiza o preço de um produto.
     *
     * @param id        ID do produto.
     * @param novoPreco Novo preço.
     * @return ProdutoResponse com o novo valor.
     */
    @PatchMapping("/{id}/preco")
    public ResponseEntity<ProdutoResponse> atualizarPreco(@PathVariable Long id,
                                                          @RequestParam BigDecimal novoPreco) {
        return ResponseEntity.ok(produtoService.atualizarPreco(id, novoPreco));
    }

    /**
     * Verifica se um SKU está disponível (não cadastrado ainda).
     *
     * @param sku Código SKU.
     * @return true se disponível, false se já em uso.
     */
    @GetMapping("/verificar-sku/{sku}")
    public ResponseEntity<Boolean> verificarDisponibilidadeSku(@PathVariable String sku) {
        return ResponseEntity.ok(produtoService.verificarDisponibilidadeSku(sku));
    }
}