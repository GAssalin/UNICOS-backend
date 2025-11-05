package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.TransacaoEstoqueListDTO;
import br.com.unicos.ms_estoque.dto.TransacaoEstoqueRequest;
import br.com.unicos.ms_estoque.dto.TransacaoEstoqueResponse;
import br.com.unicos.ms_estoque.enums.TipoTransacao;
import br.com.unicos.ms_estoque.service.TransacaoEstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das transações de estoque.
 * <p>
 * Permite operações de criação, exclusão e consulta de transações,
 * como entradas, saídas, transferências e ajustes de estoque.
 */
@RestController
@RequestMapping("/v1/transacoes-estoque")
@RequiredArgsConstructor
public class TransacaoEstoqueController {

    private final TransacaoEstoqueService transacaoEstoqueService;

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cria uma nova transação de estoque.
     *
     * @param request DTO com os dados da transação.
     * @return Dados da transação criada.
     */
    @PostMapping
    public ResponseEntity<TransacaoEstoqueResponse> criar(@Valid @RequestBody TransacaoEstoqueRequest request) {
        TransacaoEstoqueResponse response = transacaoEstoqueService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Exclui uma transação de estoque pelo ID.
     *
     * @param id ID da transação.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        transacaoEstoqueService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS
    // ==================================

    /**
     * Lista todas as transações de estoque.
     *
     * @return Lista de transações.
     */
    @GetMapping
    public ResponseEntity<List<TransacaoEstoqueListDTO>> listarTodas() {
        return ResponseEntity.ok(transacaoEstoqueService.listarTodas());
    }

    /**
     * Busca uma transação específica pelo ID.
     *
     * @param id ID da transação.
     * @return Dados da transação.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoEstoqueResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transacaoEstoqueService.buscarPorId(id));
    }

    /**
     * Lista transações de um tipo específico (ex: ENTRADA, SAIDA, AJUSTE).
     *
     * @param tipo Tipo de transação.
     * @return Lista de transações do tipo informado.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<TransacaoEstoqueListDTO>> buscarPorTipo(@PathVariable TipoTransacao tipo) {
        return ResponseEntity.ok(transacaoEstoqueService.buscarPorTipo(tipo));
    }

    /**
     * Lista transações realizadas em um intervalo de datas.
     *
     * @param inicio Data/hora inicial.
     * @param fim    Data/hora final.
     * @return Lista de transações no período.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<TransacaoEstoqueListDTO>> buscarPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        return ResponseEntity.ok(transacaoEstoqueService.buscarPorPeriodo(inicio, fim));
    }

    /**
     * Conta o número de transações de determinado tipo em um período específico.
     *
     * @param tipo   Tipo de transação.
     * @param inicio Data/hora inicial.
     * @param fim    Data/hora final.
     * @return Quantidade de transações.
     */
    @GetMapping("/contar")
    public ResponseEntity<Long> contarPorTipoEPeriodo(
            @RequestParam TipoTransacao tipo,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        long total = transacaoEstoqueService.contarPorTipoEPeriodo(tipo, inicio, fim);
        return ResponseEntity.ok(total);
    }
}
