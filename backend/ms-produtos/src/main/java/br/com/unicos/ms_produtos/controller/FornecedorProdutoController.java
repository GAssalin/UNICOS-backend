package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.FornecedorProdutoListDTO;
import br.com.unicos.ms_produtos.dto.FornecedorProdutoRequest;
import br.com.unicos.ms_produtos.dto.FornecedorProdutoResponse;
import br.com.unicos.ms_produtos.service.FornecedorProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos vínculos
 * entre fornecedores e produtos.
 */
@RestController
@RequestMapping("/v1/fornecedores-produtos")
public class FornecedorProdutoController {

    private final FornecedorProdutoService fornecedorProdutoService;

    public FornecedorProdutoController(FornecedorProdutoService fornecedorProdutoService) {
        this.fornecedorProdutoService = fornecedorProdutoService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo vínculo entre fornecedor e produto.
     *
     * @param request Dados do vínculo a ser criado.
     * @return FornecedorProdutoResponse criado.
     */
    @PostMapping
    public ResponseEntity<FornecedorProdutoResponse> criar(@Valid @RequestBody FornecedorProdutoRequest request) {
        FornecedorProdutoResponse response = fornecedorProdutoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um vínculo existente entre fornecedor e produto.
     *
     * @param id ID do vínculo.
     * @param request Dados atualizados.
     * @return FornecedorProdutoResponse atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorProdutoResponse> atualizar(@PathVariable Long id,
                                                               @Valid @RequestBody FornecedorProdutoRequest request) {
        FornecedorProdutoResponse response = fornecedorProdutoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um vínculo específico por ID.
     *
     * @param id ID do vínculo.
     * @return FornecedorProdutoResponse, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorProdutoResponse> buscarPorId(@PathVariable Long id) {
        return fornecedorProdutoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os vínculos cadastrados.
     *
     * @return Lista completa de FornecedorProdutoResponse.
     */
    @GetMapping
    public ResponseEntity<List<FornecedorProdutoResponse>> listarTodos() {
        return ResponseEntity.ok(fornecedorProdutoService.listarTodos());
    }

    /**
     * Remove um vínculo entre fornecedor e produto.
     *
     * @param id ID do vínculo.
     * @return Resposta 204 (sem conteúdo).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fornecedorProdutoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔍 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista todos os vínculos de um determinado produto.
     *
     * @param produtoId ID do produto.
     * @return Lista de fornecedores vinculados ao produto.
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<FornecedorProdutoListDTO>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(fornecedorProdutoService.listarPorProduto(produtoId));
    }

    /**
     * Lista todos os vínculos de um determinado fornecedor.
     *
     * @param fornecedorId ID do fornecedor.
     * @return Lista de produtos vinculados ao fornecedor.
     */
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<FornecedorProdutoListDTO>> listarPorFornecedor(@PathVariable Long fornecedorId) {
        return ResponseEntity.ok(fornecedorProdutoService.listarPorFornecedor(fornecedorId));
    }

    /**
     * Atualiza apenas o preço de custo de um vínculo existente.
     *
     * @param id ID do vínculo.
     * @param novoPrecoCusto Novo valor.
     * @return FornecedorProdutoResponse atualizado.
     */
    @PatchMapping("/{id}/preco-custo")
    public ResponseEntity<FornecedorProdutoResponse> atualizarPrecoCusto(@PathVariable Long id,
                                                                         @RequestParam BigDecimal novoPrecoCusto) {
        FornecedorProdutoResponse response = fornecedorProdutoService.atualizarPrecoCusto(id, novoPrecoCusto);
        return ResponseEntity.ok(response);
    }

    /**
     * Verifica se já existe um vínculo entre fornecedor e produto.
     *
     * @param fornecedorId ID do fornecedor.
     * @param produtoId ID do produto.
     * @return true se o vínculo existir, false caso contrário.
     */
    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarVinculo(@RequestParam Long fornecedorId,
                                                    @RequestParam Long produtoId) {
        boolean existe = fornecedorProdutoService.existeVinculo(fornecedorId, produtoId);
        return ResponseEntity.ok(existe);
    }
}