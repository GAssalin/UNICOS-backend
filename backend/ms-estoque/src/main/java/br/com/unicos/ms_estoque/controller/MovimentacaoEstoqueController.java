package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.MovimentacaoEstoqueRequest;
import br.com.unicos.ms_estoque.dto.MovimentacaoEstoqueResponse;
import br.com.unicos.ms_estoque.service.MovimentacaoEstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das movimentações de estoque.
 * <p>
 * Permite o registro de produtos movimentados em cada transação de estoque
 * (entrada, saída, ajuste ou transferência), além de consultas detalhadas
 * por produto, transação ou período.
 */
@RestController
@RequestMapping("/v1/movimentacoes-estoque")
@RequiredArgsConstructor
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Registra uma nova movimentação de produto em uma transação de estoque.
     *
     * @param request DTO com os dados da movimentação.
     * @return Movimentação criada.
     */
    @PostMapping
    public ResponseEntity<MovimentacaoEstoqueResponse> criar(@Valid @RequestBody MovimentacaoEstoqueRequest request) {
        MovimentacaoEstoqueResponse response = movimentacaoEstoqueService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Exclui uma movimentação de estoque pelo ID.
     *
     * @param id ID da movimentação.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        movimentacaoEstoqueService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    /**
     * Lista todas as movimentações pertencentes a uma transação de estoque.
     *
     * @param transacaoId ID da transação.
     * @return Lista de movimentações vinculadas à transação.
     */
    @GetMapping("/transacao/{transacaoId}")
    public ResponseEntity<List<MovimentacaoEstoqueResponse>> listarPorTransacao(@PathVariable Long transacaoId) {
        return ResponseEntity.ok(movimentacaoEstoqueService.listarPorTransacao(transacaoId));
    }

    /**
     * Lista todas as movimentações de um produto específico.
     *
     * @param produtoEstoqueId ID do produto em estoque.
     * @return Lista de movimentações relacionadas ao produto.
     */
    @GetMapping("/produto/{produtoEstoqueId}")
    public ResponseEntity<List<MovimentacaoEstoqueResponse>> listarPorProduto(@PathVariable Long produtoEstoqueId) {
        return ResponseEntity.ok(movimentacaoEstoqueService.listarPorProduto(produtoEstoqueId));
    }

    /**
     * Calcula a quantidade total movimentada de um produto em um período.
     *
     * @param produtoEstoqueId ID do produto em estoque.
     * @param inicio           Data/hora inicial do intervalo.
     * @param fim              Data/hora final do intervalo.
     * @return Quantidade total movimentada no período.
     */
    @GetMapping("/produto/{produtoEstoqueId}/total")
    public ResponseEntity<Double> calcularTotalMovimentado(
            @PathVariable Long produtoEstoqueId,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        Double total = movimentacaoEstoqueService.calcularTotalMovimentado(produtoEstoqueId, inicio, fim);
        return ResponseEntity.ok(total);
    }
}
