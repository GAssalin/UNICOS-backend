package br.com.unicos.ms_pagamento.controller;

import br.com.unicos.ms_pagamento.dto.PagamentoRequest;
import br.com.unicos.ms_pagamento.dto.PagamentoResponse;
import br.com.unicos.ms_pagamento.enums.StatusPagamento;
import br.com.unicos.ms_pagamento.enums.TipoTransacao;
import br.com.unicos.ms_pagamento.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento de pagamentos.
 *
 * Fornece endpoints para operações de CRUD, consultas específicas
 * e controle financeiro de status e valores.
 */
@RestController
@RequestMapping("/v1/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Registra um novo pagamento.
     *
     * @param request Dados do pagamento a ser criado.
     * @return PagamentoResponse com os dados do pagamento criado.
     */
    @PostMapping
    public ResponseEntity<PagamentoResponse> criarPagamento(@Valid @RequestBody PagamentoRequest request) {
        PagamentoResponse response = pagamentoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um pagamento existente.
     *
     * @param id      Identificador do pagamento.
     * @param request Dados atualizados do pagamento.
     * @return PagamentoResponse atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PagamentoResponse> atualizarPagamento(@PathVariable Long id,
                                                                @Valid @RequestBody PagamentoRequest request) {
        PagamentoResponse response = pagamentoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um pagamento pelo seu ID.
     *
     * @param id Identificador do pagamento.
     * @return PagamentoResponse encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscarPorId(@PathVariable Long id) {
        return pagamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os pagamentos cadastrados.
     *
     * @return Lista de PagamentoResponse.
     */
    @GetMapping
    public ResponseEntity<List<PagamentoResponse>> listarTodos() {
        List<PagamentoResponse> pagamentos = pagamentoService.listarTodos();
        return ResponseEntity.ok(pagamentos);
    }

    /**
     * Remove um pagamento pelo seu ID.
     *
     * @param id Identificador do pagamento.
     * @return Resposta 204 (sem conteúdo).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPagamento(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista pagamentos com base no status informado.
     *
     * @param status Status do pagamento (PENDENTE, PAGO, CANCELADO, etc.).
     * @return Lista de PagamentoResponse com o status informado.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PagamentoResponse>> listarPorStatus(@PathVariable StatusPagamento status) {
        List<PagamentoResponse> pagamentos = pagamentoService.listarPorStatus(status);
        return ResponseEntity.ok(pagamentos);
    }

    /**
     * Lista pagamentos de acordo com o tipo de transação.
     *
     * @param tipo Tipo da transação (VENDA, COMPRA, OUTROS).
     * @return Lista de pagamentos do tipo informado.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<PagamentoResponse>> listarPorTipoTransacao(@PathVariable TipoTransacao tipo) {
        List<PagamentoResponse> pagamentos = pagamentoService.listarPorTipoTransacao(tipo);
        return ResponseEntity.ok(pagamentos);
    }

    /**
     * Lista pagamentos realizados entre duas datas.
     *
     * @param inicio Data inicial.
     * @param fim    Data final.
     * @return Lista de pagamentos dentro do intervalo informado.
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<PagamentoResponse>> listarPorPeriodo(@RequestParam LocalDate inicio,
                                                                    @RequestParam LocalDate fim) {
        List<PagamentoResponse> pagamentos = pagamentoService.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(pagamentos);
    }

    /**
     * Lista pagamentos em atraso (vencidos e não pagos).
     *
     * @return Lista de pagamentos atrasados.
     */
    @GetMapping("/atrasados")
    public ResponseEntity<List<PagamentoResponse>> listarAtrasados() {
        List<PagamentoResponse> pagamentos = pagamentoService.listarAtrasados();
        return ResponseEntity.ok(pagamentos);
    }

    // ==================================
    // 💼 OPERAÇÕES DE NEGÓCIO
    // ==================================

    /**
     * Atualiza o status de um pagamento.
     *
     * @param id     ID do pagamento.
     * @param status Novo status a ser definido.
     * @return PagamentoResponse atualizado.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<PagamentoResponse> atualizarStatus(@PathVariable Long id,
                                                             @RequestParam StatusPagamento status) {
        PagamentoResponse response = pagamentoService.atualizarStatus(id, status);
        return ResponseEntity.ok(response);
    }

    /**
     * Calcula o valor total de todos os pagamentos quitados.
     *
     * @return Valor total de pagamentos pagos.
     */
    @GetMapping("/total/pago")
    public ResponseEntity<BigDecimal> calcularTotalPago() {
        return ResponseEntity.ok(pagamentoService.calcularTotalPago());
    }

    /**
     * Calcula o valor total de todos os pagamentos pendentes.
     *
     * @return Valor total de pagamentos pendentes.
     */
    @GetMapping("/total/pendente")
    public ResponseEntity<BigDecimal> calcularTotalPendente() {
        return ResponseEntity.ok(pagamentoService.calcularTotalPendente());
    }
}
