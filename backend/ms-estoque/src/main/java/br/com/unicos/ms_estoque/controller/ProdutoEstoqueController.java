package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.ProdutoEstoqueListDTO;
import br.com.unicos.ms_estoque.dto.ProdutoEstoqueRequest;
import br.com.unicos.ms_estoque.dto.ProdutoEstoqueResponse;
import br.com.unicos.ms_estoque.service.ProdutoEstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos produtos armazenados nos estoques.
 * <p>
 * Permite operações de criação, atualização, listagem, exclusão e consultas específicas
 * sobre os níveis de estoque de cada produto.
 */
@RestController
@RequestMapping("/v1/produtos-estoque")
@RequiredArgsConstructor
public class ProdutoEstoqueController {

    private final ProdutoEstoqueService produtoEstoqueService;

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo registro de produto em um local de estoque.
     *
     * @param request DTO com os dados do produto em estoque.
     * @return Dados do produto criado.
     */
    @PostMapping
    public ResponseEntity<ProdutoEstoqueResponse> criar(@Valid @RequestBody ProdutoEstoqueRequest request) {
        ProdutoEstoqueResponse response = produtoEstoqueService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um produto existente em um local de estoque.
     *
     * @param id      ID do produto em estoque.
     * @param request DTO com os novos dados.
     * @return Produto atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoEstoqueResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoEstoqueRequest request
    ) {
        ProdutoEstoqueResponse response = produtoEstoqueService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui um produto do estoque.
     *
     * @param id ID do produto em estoque.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoEstoqueService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    /**
     * Lista todos os produtos em estoque.
     *
     * @return Lista de produtos e seus saldos.
     */
    @GetMapping
    public ResponseEntity<List<ProdutoEstoqueListDTO>> listarTodos() {
        return ResponseEntity.ok(produtoEstoqueService.listarTodos());
    }

    /**
     * Busca um produto em estoque pelo ID.
     *
     * @param id ID do produto em estoque.
     * @return Dados completos do produto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoEstoqueResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoEstoqueService.buscarPorId(id));
    }

    /**
     * Lista os produtos de um local de estoque específico.
     *
     * @param estoqueLocalId ID do local de estoque.
     * @return Lista de produtos armazenados no local.
     */
    @GetMapping("/local/{estoqueLocalId}")
    public ResponseEntity<List<ProdutoEstoqueListDTO>> listarPorEstoque(@PathVariable Long estoqueLocalId) {
        return ResponseEntity.ok(produtoEstoqueService.listarPorEstoque(estoqueLocalId));
    }

    /**
     * Lista os produtos cujo estoque está abaixo do nível mínimo configurado.
     *
     * @return Lista de produtos com estoque baixo.
     */
    @GetMapping("/baixo")
    public ResponseEntity<List<ProdutoEstoqueListDTO>> listarEstoqueBaixo() {
        return ResponseEntity.ok(produtoEstoqueService.listarEstoqueBaixo());
    }

    /**
     * Lista os produtos cujo estoque excede o nível máximo configurado.
     *
     * @return Lista de produtos com estoque excedente.
     */
    @GetMapping("/excedente")
    public ResponseEntity<List<ProdutoEstoqueListDTO>> listarEstoqueExcedente() {
        return ResponseEntity.ok(produtoEstoqueService.listarEstoqueExcedente());
    }
}
