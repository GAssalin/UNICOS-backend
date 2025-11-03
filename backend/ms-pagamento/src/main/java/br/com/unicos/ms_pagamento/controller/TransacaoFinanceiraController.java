package br.com.unicos.ms_pagamento.controller;

import br.com.unicos.ms_pagamento.dto.TransacaoFinanceiraRequest;
import br.com.unicos.ms_pagamento.dto.TransacaoFinanceiraResponse;
import br.com.unicos.ms_pagamento.enums.StatusTransacao;
import br.com.unicos.ms_pagamento.enums.TipoFormaPagamento;
import br.com.unicos.ms_pagamento.service.TransacaoFinanceiraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das transações financeiras.
 *
 * Fornece endpoints para operações de CRUD, consultas específicas e
 * controle de status e estatísticas das transações.
 */
@RestController
@RequestMapping("/v1/transacoes-financeiras")
public class TransacaoFinanceiraController {

    private final TransacaoFinanceiraService transacaoFinanceiraService;

    public TransacaoFinanceiraController(TransacaoFinanceiraService transacaoFinanceiraService) {
        this.transacaoFinanceiraService = transacaoFinanceiraService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cadastra uma nova transação financeira.
     *
     * @param request Dados da transação a ser criada.
     * @return TransacaoFinanceiraResponse com os dados da transação criada.
     */
    @PostMapping
    public ResponseEntity<TransacaoFinanceiraResponse> criarTransacao(@Valid @RequestBody TransacaoFinanceiraRequest request) {
        TransacaoFinanceiraResponse response = transacaoFinanceiraService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de uma transação existente.
     *
     * @param id      Identificador da transação.
     * @param request Dados atualizados da transação.
     * @return TransacaoFinanceiraResponse atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransacaoFinanceiraResponse> atualizarTransacao(@PathVariable Long id,
                                                                          @Valid @RequestBody TransacaoFinanceiraRequest request) {
        TransacaoFinanceiraResponse response = transacaoFinanceiraService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma transação financeira pelo seu ID.
     *
     * @param id Identificador da transação.
     * @return TransacaoFinanceiraResponse encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoFinanceiraResponse> buscarPorId(@PathVariable Long id) {
        return transacaoFinanceiraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as transações financeiras cadastradas.
     *
     * @return Lista de TransacaoFinanceiraResponse.
     */
    @GetMapping
    public ResponseEntity<List<TransacaoFinanceiraResponse>> listarTodas() {
        List<TransacaoFinanceiraResponse> transacoes = transacaoFinanceiraService.listarTodas();
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Remove uma transação pelo seu ID.
     *
     * @param id Identificador da transação.
     * @return Resposta 204 (sem conteúdo).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTransacao(@PathVariable Long id) {
        transacaoFinanceiraService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Busca uma transação pelo código identificador.
     *
     * @param codigoTransacao Código único da transação.
     * @return TransacaoFinanceiraResponse correspondente.
     */
    @GetMapping("/codigo/{codigoTransacao}")
    public ResponseEntity<TransacaoFinanceiraResponse> buscarPorCodigo(@PathVariable String codigoTransacao) {
        return transacaoFinanceiraService.buscarPorCodigo(codigoTransacao)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista transações de acordo com o status informado.
     *
     * @param status Status da transação (PROCESSANDO, CONFIRMADA, FALHA).
     * @return Lista de transações com o status informado.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransacaoFinanceiraResponse>> listarPorStatus(@PathVariable StatusTransacao status) {
        List<TransacaoFinanceiraResponse> transacoes = transacaoFinanceiraService.listarPorStatus(status);
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Lista transações de um tipo específico de pagamento.
     *
     * @param tipo Tipo de forma de pagamento (PIX, CARTÃO, BOLETO, etc.).
     * @return Lista de transações do tipo informado.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<TransacaoFinanceiraResponse>> listarPorTipo(@PathVariable TipoFormaPagamento tipo) {
        List<TransacaoFinanceiraResponse> transacoes = transacaoFinanceiraService.listarPorTipo(tipo);
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Lista transações realizadas dentro de um intervalo de datas.
     *
     * @param inicio Data/hora inicial.
     * @param fim    Data/hora final.
     * @return Lista de transações no período informado.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<TransacaoFinanceiraResponse>> listarPorPeriodo(@RequestParam LocalDateTime inicio,
                                                                              @RequestParam LocalDateTime fim) {
        List<TransacaoFinanceiraResponse> transacoes = transacaoFinanceiraService.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(transacoes);
    }

    // ==================================
    // 💼 OPERAÇÕES DE NEGÓCIO
    // ==================================

    /**
     * Atualiza o status de uma transação financeira.
     *
     * @param id     ID da transação.
     * @param status Novo status a ser definido.
     * @return TransacaoFinanceiraResponse atualizada.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<TransacaoFinanceiraResponse> atualizarStatus(@PathVariable Long id,
                                                                       @RequestParam StatusTransacao status) {
        TransacaoFinanceiraResponse response = transacaoFinanceiraService.atualizarStatus(id, status);
        return ResponseEntity.ok(response);
    }

    /**
     * Conta quantas transações apresentaram falha.
     *
     * @return Quantidade total de transações com falha.
     */
    @GetMapping("/falhas")
    public ResponseEntity<Long> contarTransacoesFalhas() {
        Long total = transacaoFinanceiraService.contarTransacoesFalhas();
        return ResponseEntity.ok(total);
    }
}
