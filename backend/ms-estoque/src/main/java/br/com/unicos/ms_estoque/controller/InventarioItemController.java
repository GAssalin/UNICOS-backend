package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.InventarioItemRequest;
import br.com.unicos.ms_estoque.dto.InventarioItemResponse;
import br.com.unicos.ms_estoque.service.InventarioItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos itens de inventário.
 * <p>
 * Permite operações de criação, atualização, exclusão e consultas
 * sobre os produtos contados durante o inventário físico.
 */
@RestController
@RequestMapping("/v1/inventarios-itens")
@RequiredArgsConstructor
public class InventarioItemController {

    private final InventarioItemService inventarioItemService;

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo item vinculado a um inventário.
     *
     * @param request DTO com os dados do item.
     * @return Item criado.
     */
    @PostMapping
    public ResponseEntity<InventarioItemResponse> criar(@Valid @RequestBody InventarioItemRequest request) {
        InventarioItemResponse response = inventarioItemService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de um item existente do inventário.
     *
     * @param id      ID do item.
     * @param request DTO com novos dados.
     * @return Item atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventarioItemResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioItemRequest request
    ) {
        InventarioItemResponse response = inventarioItemService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui um item do inventário.
     *
     * @param id ID do item.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        inventarioItemService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    /**
     * Lista todos os itens pertencentes a um inventário.
     *
     * @param inventarioId ID do inventário.
     * @return Lista de itens vinculados ao inventário.
     */
    @GetMapping("/inventario/{inventarioId}")
    public ResponseEntity<List<InventarioItemResponse>> listarPorInventario(@PathVariable Long inventarioId) {
        return ResponseEntity.ok(inventarioItemService.listarPorInventario(inventarioId));
    }

    /**
     * Lista os itens do inventário que possuem divergências
     * entre quantidade contada e registrada.
     *
     * @param inventarioId ID do inventário.
     * @return Lista de itens divergentes.
     */
    @GetMapping("/inventario/{inventarioId}/divergentes")
    public ResponseEntity<List<InventarioItemResponse>> listarItensDivergentes(@PathVariable Long inventarioId) {
        return ResponseEntity.ok(inventarioItemService.listarItensDivergentes(inventarioId));
    }

    /**
     * Calcula a diferença total (contada - registrada)
     * de todos os itens de um inventário.
     *
     * @param inventarioId ID do inventário.
     * @return Diferença total entre quantidades contadas e registradas.
     */
    @GetMapping("/inventario/{inventarioId}/diferenca-total")
    public ResponseEntity<Double> calcularDiferencaTotal(@PathVariable Long inventarioId) {
        Double diferenca = inventarioItemService.calcularDiferencaTotal(inventarioId);
        return ResponseEntity.ok(diferenca);
    }
}
