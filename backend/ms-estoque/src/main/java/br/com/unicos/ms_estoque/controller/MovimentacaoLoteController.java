package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.MovimentacaoLoteRequest;
import br.com.unicos.ms_estoque.dto.MovimentacaoLoteResponse;
import br.com.unicos.ms_estoque.service.MovimentacaoLoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pela associação entre movimentações de estoque e lotes.
 * <p>
 * Permite o registro, listagem e exclusão de vínculos entre movimentações e lotes,
 * garantindo a rastreabilidade de produtos em cada operação de estoque.
 */
@RestController
@RequestMapping("/v1/movimentacoes-lotes")
@RequiredArgsConstructor
public class MovimentacaoLoteController {

    private final MovimentacaoLoteService movimentacaoLoteService;

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria um novo vínculo entre uma movimentação de estoque e um lote de produto.
     *
     * @param request DTO com os dados da movimentação e do lote.
     * @return Dados do vínculo criado.
     */
    @PostMapping
    public ResponseEntity<MovimentacaoLoteResponse> criar(@Valid @RequestBody MovimentacaoLoteRequest request) {
        MovimentacaoLoteResponse response = movimentacaoLoteService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Exclui o vínculo entre uma movimentação e um lote.
     *
     * @param id ID do vínculo a ser excluído.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        movimentacaoLoteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    /**
     * Lista todos os vínculos de lotes associados a uma movimentação específica.
     *
     * @param movimentacaoId ID da movimentação de estoque.
     * @return Lista de vínculos encontrados.
     */
    @GetMapping("/movimentacao/{movimentacaoId}")
    public ResponseEntity<List<MovimentacaoLoteResponse>> listarPorMovimentacao(@PathVariable Long movimentacaoId) {
        return ResponseEntity.ok(movimentacaoLoteService.listarPorMovimentacao(movimentacaoId));
    }

    /**
     * Lista todas as movimentações que envolvem um determinado lote.
     *
     * @param loteId ID do lote.
     * @return Lista de movimentações associadas ao lote informado.
     */
    @GetMapping("/lote/{loteId}")
    public ResponseEntity<List<MovimentacaoLoteResponse>> listarPorLote(@PathVariable Long loteId) {
        return ResponseEntity.ok(movimentacaoLoteService.listarPorLote(loteId));
    }

    /**
     * Calcula a quantidade total movimentada de um lote específico.
     *
     * @param loteId ID do lote.
     * @return Quantidade total movimentada.
     */
    @GetMapping("/lote/{loteId}/total")
    public ResponseEntity<Double> calcularQuantidadeMovimentadaPorLote(@PathVariable Long loteId) {
        Double total = movimentacaoLoteService.calcularQuantidadeMovimentadaPorLote(loteId);
        return ResponseEntity.ok(total);
    }
}
