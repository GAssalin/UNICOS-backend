package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoListDTO;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoRequest;
import br.com.unicos.ms_produtos.dto.historico_preco.HistoricoPrecoResponse;
import br.com.unicos.ms_produtos.service.HistoricoPrecoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento
 * do histórico de alterações de preços dos produtos.
 * <p>
 * Permite registrar mudanças, consultar histórico completo,
 * consultar por produto e remover registros específicos.
 */
@RestController
@RequestMapping("/v1/historico-precos")
@RequiredArgsConstructor
public class HistoricoPrecoController {

    private final HistoricoPrecoService historicoPrecoService;

    // ============================================================
    // 🔹 Criar registro de histórico de preço
    // ============================================================

    /**
     * Registra uma nova alteração de preço para um produto.
     *
     * @param produtoId ID do produto.
     * @param request   dados da alteração.
     * @return registro criado.
     */
    @PostMapping("/produto/{produtoId}")
    public ResponseEntity<HistoricoPrecoResponse> salvar(
            @PathVariable Long produtoId,
            @Valid @RequestBody HistoricoPrecoRequest request) {

        HistoricoPrecoResponse response = historicoPrecoService.salvar(produtoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // 🔹 Buscar por ID do histórico
    // ============================================================

    /**
     * Busca um registro específico de histórico pelo ID.
     *
     * @param id ID do histórico.
     * @return registro encontrado ou 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoricoPrecoResponse> buscarPorId(@PathVariable Long id) {

        Optional<HistoricoPrecoResponse> resultado =
                historicoPrecoService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // 🔹 Listar todos os registros (ordenados por data desc)
    // ============================================================

    /**
     * Lista todos os registros de histórico de preço existentes.
     *
     * @return lista completa.
     */
    @GetMapping
    public ResponseEntity<List<HistoricoPrecoResponse>> listarTodos() {
        return ResponseEntity.ok(historicoPrecoService.listarTodos());
    }

    // ============================================================
    // 🔹 Listar registros por produto
    // ============================================================

    /**
     * Lista todo o histórico de preço de um produto.
     *
     * @param produtoId ID do produto.
     * @return lista de alterações.
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<HistoricoPrecoResponse>> listarPorProduto(
            @PathVariable Long produtoId) {

        return ResponseEntity.ok(historicoPrecoService.listarPorProduto(produtoId));
    }

    // ============================================================
    // 🔹 Listar os 10 últimos registros por produto (resumido)
    // ============================================================

    /**
     * Lista os últimos 10 registros de histórico de preço de um produto.
     * (DTO simplificado)
     *
     * @param produtoId ID do produto.
     * @return lista reduzida (10 itens).
     */
    @GetMapping("/produto/{produtoId}/ultimos")
    public ResponseEntity<List<HistoricoPrecoListDTO>> listarUltimosPorProduto(
            @PathVariable Long produtoId) {

        return ResponseEntity.ok(historicoPrecoService.listarUltimosPorProduto(produtoId));
    }

    // ============================================================
    // 🔹 Deletar registro de histórico
    // ============================================================

    /**
     * Remove um registro de histórico de preço.
     *
     * @param id ID do registro.
     * @return 204 se removido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        historicoPrecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
