package br.com.unicos.ms_pagamento.controller.core;

import br.com.unicos.ms_pagamento.dto.core.ParcelaPagamentoRequest;
import br.com.unicos.ms_pagamento.dto.core.ParcelaPagamentoResponse;
import br.com.unicos.ms_pagamento.enums.StatusParcela;
import br.com.unicos.ms_pagamento.service.core.ParcelaPagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das parcelas de pagamento.
 *
 * Fornece endpoints para operações de CRUD, consultas específicas
 * e operações de quitação e controle de status das parcelas.
 */
@RestController
@RequestMapping("/v1/parcelas-pagamento")
public class ParcelaPagamentoController {

    private final ParcelaPagamentoService parcelaPagamentoService;

    public ParcelaPagamentoController(ParcelaPagamentoService parcelaPagamentoService) {
        this.parcelaPagamentoService = parcelaPagamentoService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cadastra uma nova parcela de pagamento.
     *
     * @param request Dados da parcela.
     * @return ParcelaPagamentoResponse representando a parcela criada.
     */
    @PostMapping
    public ResponseEntity<ParcelaPagamentoResponse> criarParcela(@Valid @RequestBody ParcelaPagamentoRequest request) {
        ParcelaPagamentoResponse response = parcelaPagamentoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma parcela existente.
     *
     * @param id      Identificador da parcela.
     * @param request Dados atualizados da parcela.
     * @return ParcelaPagamentoResponse atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ParcelaPagamentoResponse> atualizarParcela(@PathVariable Long id,
                                                                     @Valid @RequestBody ParcelaPagamentoRequest request) {
        ParcelaPagamentoResponse response = parcelaPagamentoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma parcela pelo seu ID.
     *
     * @param id Identificador da parcela.
     * @return ParcelaPagamentoResponse encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParcelaPagamentoResponse> buscarPorId(@PathVariable Long id) {
        return parcelaPagamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as parcelas cadastradas.
     *
     * @return Lista de ParcelaPagamentoResponse.
     */
    @GetMapping
    public ResponseEntity<List<ParcelaPagamentoResponse>> listarTodas() {
        List<ParcelaPagamentoResponse> parcelas = parcelaPagamentoService.listarTodas();
        return ResponseEntity.ok(parcelas);
    }

    /**
     * Remove uma parcela pelo seu ID.
     *
     * @param id Identificador da parcela.
     * @return Resposta 204 (sem conteúdo).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarParcela(@PathVariable Long id) {
        parcelaPagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista todas as parcelas de um pagamento específico.
     *
     * @param pagamentoId ID do pagamento.
     * @return Lista de parcelas vinculadas ao pagamento informado.
     */
    @GetMapping("/pagamento/{pagamentoId}")
    public ResponseEntity<List<ParcelaPagamentoResponse>> listarPorPagamento(@PathVariable Long pagamentoId) {
        List<ParcelaPagamentoResponse> parcelas = parcelaPagamentoService.listarPorPagamento(pagamentoId);
        return ResponseEntity.ok(parcelas);
    }

    /**
     * Lista parcelas de acordo com o status informado.
     *
     * @param status Status da parcela (PENDENTE, QUITADA, ATRASADA).
     * @return Lista de parcelas com o status informado.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ParcelaPagamentoResponse>> listarPorStatus(@PathVariable StatusParcela status) {
        List<ParcelaPagamentoResponse> parcelas = parcelaPagamentoService.listarPorStatus(status);
        return ResponseEntity.ok(parcelas);
    }

    /**
     * Lista parcelas com vencimento anterior à data informada.
     *
     * @param data Data limite de vencimento.
     * @return Lista de parcelas vencidas antes da data informada.
     */
    @GetMapping("/vencidas")
    public ResponseEntity<List<ParcelaPagamentoResponse>> listarVencidasAntesDe(@RequestParam LocalDate data) {
        List<ParcelaPagamentoResponse> parcelas = parcelaPagamentoService.listarVencidasAntesDe(data);
        return ResponseEntity.ok(parcelas);
    }

    /**
     * Lista parcelas em atraso (vencidas e não quitadas).
     *
     * @return Lista de parcelas em atraso.
     */
    @GetMapping("/atrasadas")
    public ResponseEntity<List<ParcelaPagamentoResponse>> listarAtrasadas() {
        List<ParcelaPagamentoResponse> parcelas = parcelaPagamentoService.listarAtrasadas();
        return ResponseEntity.ok(parcelas);
    }

    // ==================================
    // 💼 OPERAÇÕES DE NEGÓCIO
    // ==================================

    /**
     * Quita uma parcela (marca como paga e define data de pagamento atual).
     *
     * @param id ID da parcela.
     * @return ParcelaPagamentoResponse com status atualizado.
     */
    @PatchMapping("/{id}/quitar")
    public ResponseEntity<ParcelaPagamentoResponse> quitarParcela(@PathVariable Long id) {
        ParcelaPagamentoResponse response = parcelaPagamentoService.quitarParcela(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza o status de uma parcela.
     *
     * @param id     ID da parcela.
     * @param status Novo status a ser definido.
     * @return ParcelaPagamentoResponse atualizada.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ParcelaPagamentoResponse> atualizarStatus(@PathVariable Long id,
                                                                    @RequestParam StatusParcela status) {
        ParcelaPagamentoResponse response = parcelaPagamentoService.atualizarStatus(id, status);
        return ResponseEntity.ok(response);
    }
}
