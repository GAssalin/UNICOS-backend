package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.produto.ProdutoRequest;
import br.com.unicos.ms_produtos.dto.produto.ProdutoResponse;
import br.com.unicos.ms_produtos.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de produtos.
 * <p>
 * Possui endpoints para criar, atualizar, consultar, ativar, inativar,
 * atualizar preço e filtrar produtos por diversos critérios.
 */
@RestController
@RequestMapping("/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    // ============================================================
    // 🔹 CRUD PRINCIPAL
    // ============================================================

    /**
     * Cadastra um novo produto.
     *
     * @param request dados do produto.
     * @return produto criado.
     */
    @PostMapping
    public ResponseEntity<ProdutoResponse> salvar(
            @Valid @RequestBody ProdutoRequest request) {

        ProdutoResponse response = produtoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza completamente os dados de um produto.
     *
     * @param id      ID do produto.
     * @param request dados atualizados.
     * @return produto atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request) {

        ProdutoResponse response = produtoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um produto pelo ID.
     *
     * @param id ID do produto.
     * @return 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // 🔹 CONSULTAS GERAIS
    // ============================================================

    /**
     * Busca um produto pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {

        Optional<ProdutoResponse> resultado = produtoService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os produtos.
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    /**
     * Busca produtos pelo nome (contém, ignore case).
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponse>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }

    /**
     * Busca produto pelo SKU.
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProdutoResponse> buscarPorSku(@PathVariable String sku) {

        Optional<ProdutoResponse> resultado = produtoService.buscarPorSku(sku);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // 🔹 CONSULTAS POR FILTROS
    // ============================================================

    /**
     * Lista produtos de uma categoria específica.
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorCategoria(
            @PathVariable Long categoriaId) {

        return ResponseEntity.ok(produtoService.listarPorCategoria(categoriaId));
    }

    /**
     * Lista produtos de uma marca específica.
     */
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorMarca(
            @PathVariable Long marcaId) {

        return ResponseEntity.ok(produtoService.listarPorMarca(marcaId));
    }

    /**
     * Lista produtos ativos.
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<ProdutoResponse>> listarAtivos() {
        return ResponseEntity.ok(produtoService.listarAtivos());
    }

    /**
     * Lista produtos inativos.
     */
    @GetMapping("/inativos")
    public ResponseEntity<List<ProdutoResponse>> listarInativos() {
        return ResponseEntity.ok(produtoService.listarInativos());
    }

    /**
     * Lista produtos dentro de uma faixa de preço.
     */
    @GetMapping("/preco")
    public ResponseEntity<List<ProdutoResponse>> listarPorFaixaPreco(
            @RequestParam BigDecimal minimo,
            @RequestParam BigDecimal maximo) {

        return ResponseEntity.ok(produtoService.listarPorFaixaDePreco(minimo, maximo));
    }

    // ============================================================
    // 🔹 ALTERAÇÃO DE ESTADO (ATIVAR / INATIVAR)
    // ============================================================

    /**
     * Ativa um produto.
     */
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ProdutoResponse> ativarProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.ativarProduto(id));
    }

    /**
     * Inativa um produto.
     */
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<ProdutoResponse> inativarProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.inativarProduto(id));
    }

    // ============================================================
    // 🔹 PREÇO
    // ============================================================

    /**
     * Atualiza apenas o preço de venda do produto.
     *
     * @param id        ID do produto.
     * @param novoPreco novo valor para o preço de venda.
     */
    @PatchMapping("/{id}/preco")
    public ResponseEntity<ProdutoResponse> atualizarPreco(
            @PathVariable Long id,
            @RequestParam BigDecimal novoPreco) {

        ProdutoResponse response = produtoService.atualizarPreco(id, novoPreco);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 SKU — Validação
    // ============================================================

    /**
     * Verifica se um SKU está disponível.
     *
     * @param sku código SKU.
     * @return true se disponível, false se já usado.
     */
    @GetMapping("/sku/{sku}/disponivel")
    public ResponseEntity<Boolean> verificarDisponibilidadeSku(@PathVariable String sku) {

        Boolean disponivel = produtoService.verificarDisponibilidadeSku(sku);
        return ResponseEntity.ok(disponivel);
    }
}
