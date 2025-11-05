package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.InventarioEstoqueRequest;
import br.com.unicos.ms_estoque.dto.InventarioEstoqueResponse;
import br.com.unicos.ms_estoque.enums.StatusInventario;
import br.com.unicos.ms_estoque.service.InventarioEstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos inventários físicos de estoque.
 * <p>
 * Permite operações de criação, atualização, exclusão e consultas
 * por status, período e local de estoque.
 */
@RestController
@RequestMapping("/v1/inventarios-estoque")
@RequiredArgsConstructor
public class InventarioEstoqueController {

    private final InventarioEstoqueService inventarioEstoqueService;

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo inventário de estoque.
     *
     * @param request DTO com os dados do inventário.
     * @return Inventário criado.
     */
    @PostMapping
    public ResponseEntity<InventarioEstoqueResponse> criar(@Valid @RequestBody InventarioEstoqueRequest request) {
        InventarioEstoqueResponse response = inventarioEstoqueService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza as informações de um inventário existente.
     *
     * @param id      ID do inventário.
     * @param request DTO com os novos dados.
     * @return Inventário atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventarioEstoqueResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioEstoqueRequest request
    ) {
        InventarioEstoqueResponse response = inventarioEstoqueService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui um inventário pelo ID.
     *
     * @param id ID do inventário.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        inventarioEstoqueService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    /**
     * Lista todos os inventários cadastrados.
     *
     * @return Lista de inventários.
     */
    @GetMapping
    public ResponseEntity<List<InventarioEstoqueResponse>> listarTodos() {
        return ResponseEntity.ok(inventarioEstoqueService.listarTodos());
    }

    /**
     * Busca um inventário pelo ID.
     *
     * @param id ID do inventário.
     * @return Inventário correspondente.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventarioEstoqueResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioEstoqueService.buscarPorId(id));
    }

    /**
     * Lista inventários filtrados por status.
     *
     * @param status Status do inventário (ABERTO, EM_ANDAMENTO, FINALIZADO, CANCELADO).
     * @return Lista de inventários com o status informado.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InventarioEstoqueResponse>> listarPorStatus(@PathVariable StatusInventario status) {
        return ResponseEntity.ok(inventarioEstoqueService.listarPorStatus(status));
    }

    /**
     * Lista inventários abertos dentro de um intervalo de datas.
     *
     * @param inicio Data/hora inicial.
     * @param fim    Data/hora final.
     * @return Lista de inventários no período especificado.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<InventarioEstoqueResponse>> listarPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        return ResponseEntity.ok(inventarioEstoqueService.listarPorPeriodo(inicio, fim));
    }

    /**
     * Verifica se existe inventário em aberto para um local de estoque.
     *
     * @param estoqueLocalId ID do local de estoque.
     * @return {@code true} se existir inventário aberto, caso contrário {@code false}.
     */
    @GetMapping("/aberto/{estoqueLocalId}")
    public ResponseEntity<Boolean> existeInventarioAberto(@PathVariable Long estoqueLocalId) {
        boolean existe = inventarioEstoqueService.existeInventarioAberto(estoqueLocalId);
        return ResponseEntity.ok(existe);
    }
}
