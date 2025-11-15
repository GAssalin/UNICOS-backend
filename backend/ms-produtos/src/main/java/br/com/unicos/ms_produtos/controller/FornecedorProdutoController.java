package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoListDTO;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoRequest;
import br.com.unicos.ms_produtos.dto.fornecedor_produto.FornecedorProdutoResponse;
import br.com.unicos.ms_produtos.service.FornecedorProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento dos vínculos
 * entre fornecedores e produtos.
 * <p>
 * Permite cadastrar, alterar, excluir e consultar vínculos,
 * além de operações específicas como atualização de preço de custo.
 */
@RestController
@RequestMapping("/v1/fornecedores-produtos")
@RequiredArgsConstructor
public class FornecedorProdutoController {

    private final FornecedorProdutoService fornecedorProdutoService;

    // ============================================================
    // 🔹 Criar vínculo fornecedor-produto
    // ============================================================

    /**
     * Cria um novo vínculo entre fornecedor e produto.
     *
     * @param request dados do vínculo.
     * @return vínculo criado.
     */
    @PostMapping
    public ResponseEntity<FornecedorProdutoResponse> salvar(
            @Valid @RequestBody FornecedorProdutoRequest request) {

        FornecedorProdutoResponse response = fornecedorProdutoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // 🔹 Atualizar vínculo
    // ============================================================

    /**
     * Atualiza um vínculo existente entre fornecedor e produto.
     *
     * @param id      ID do vínculo.
     * @param request dados atualizados.
     * @return vínculo atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorProdutoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FornecedorProdutoRequest request) {

        FornecedorProdutoResponse response = fornecedorProdutoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Atualizar preço de custo
    // ============================================================

    /**
     * Atualiza apenas o preço de custo do vínculo.
     *
     * @param id             ID do vínculo.
     * @param novoPrecoCusto novo valor do preço de custo.
     * @return vínculo atualizado.
     */
    @PatchMapping("/{id}/preco-custo")
    public ResponseEntity<FornecedorProdutoResponse> atualizarPrecoCusto(
            @PathVariable Long id,
            @RequestParam BigDecimal novoPrecoCusto) {

        FornecedorProdutoResponse response =
                fornecedorProdutoService.atualizarPrecoCusto(id, novoPrecoCusto);

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Deletar vínculo
    // ============================================================

    /**
     * Remove um vínculo fornecedor-produto.
     *
     * @param id ID do vínculo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fornecedorProdutoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // 🔹 Buscar por ID
    // ============================================================

    /**
     * Busca um vínculo específico pelo ID.
     *
     * @param id ID do vínculo.
     * @return vínculo encontrado ou 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorProdutoResponse> buscarPorId(@PathVariable Long id) {

        Optional<FornecedorProdutoResponse> resultado =
                fornecedorProdutoService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // 🔹 Listar todos os vínculos
    // ============================================================

    /**
     * Lista todos os vínculos fornecedor-produto.
     *
     * @return lista de vínculos.
     */
    @GetMapping
    public ResponseEntity<List<FornecedorProdutoResponse>> listarTodos() {
        return ResponseEntity.ok(fornecedorProdutoService.listarTodos());
    }

    // ============================================================
    // 🔹 Listar vínculos por produto
    // ============================================================

    /**
     * Lista vínculos associados a um produto específico.
     *
     * @param produtoId ID do produto.
     * @return vínculos do produto.
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<FornecedorProdutoListDTO>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(fornecedorProdutoService.listarPorProduto(produtoId));
    }

    // ============================================================
    // 🔹 Listar vínculos por fornecedor
    // ============================================================

    /**
     * Lista vínculos associados a um fornecedor específico.
     *
     * @param fornecedorId ID do fornecedor.
     * @return vínculos do fornecedor.
     */
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<FornecedorProdutoListDTO>> listarPorFornecedor(@PathVariable Long fornecedorId) {
        return ResponseEntity.ok(fornecedorProdutoService.listarPorFornecedor(fornecedorId));
    }

    // ============================================================
    // 🔹 Verificar existência de vínculo
    // ============================================================

    /**
     * Verifica se existe um vínculo entre um fornecedor e um produto.
     *
     * @param fornecedorId ID do fornecedor.
     * @param produtoId    ID do produto.
     * @return true ou false.
     */
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeVinculo(
            @RequestParam Long fornecedorId,
            @RequestParam Long produtoId) {

        return ResponseEntity.ok(
                fornecedorProdutoService.existeVinculo(fornecedorId, produtoId)
        );
    }
}
