package br.com.unicos.ms_pagamento.controller;

import br.com.unicos.ms_pagamento.dto.FormaPagamentoRequest;
import br.com.unicos.ms_pagamento.dto.FormaPagamentoResponse;
import br.com.unicos.ms_pagamento.enums.TipoFormaPagamento;
import br.com.unicos.ms_pagamento.service.FormaPagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das formas de pagamento.
 *
 * Fornece endpoints para operações de CRUD, consultas específicas
 * e controle de ativação/inativação.
 */
@RestController
@RequestMapping("/v1/formas-pagamento")
public class FormaPagamentoController {

    private final FormaPagamentoService formaPagamentoService;

    public FormaPagamentoController(FormaPagamentoService formaPagamentoService) {
        this.formaPagamentoService = formaPagamentoService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cadastra uma nova forma de pagamento.
     *
     * @param request Dados da forma de pagamento.
     * @return FormaPagamentoResponse com os dados da forma criada.
     */
    @PostMapping
    public ResponseEntity<FormaPagamentoResponse> criarFormaPagamento(@Valid @RequestBody FormaPagamentoRequest request) {
        FormaPagamentoResponse response = formaPagamentoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de uma forma de pagamento existente.
     *
     * @param id      Identificador da forma de pagamento.
     * @param request Dados atualizados.
     * @return FormaPagamentoResponse atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormaPagamentoResponse> atualizarFormaPagamento(@PathVariable Long id,
                                                                          @Valid @RequestBody FormaPagamentoRequest request) {
        FormaPagamentoResponse response = formaPagamentoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma forma de pagamento pelo seu ID.
     *
     * @param id Identificador da forma de pagamento.
     * @return FormaPagamentoResponse encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamentoResponse> buscarPorId(@PathVariable Long id) {
        return formaPagamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as formas de pagamento cadastradas.
     *
     * @return Lista de FormaPagamentoResponse.
     */
    @GetMapping
    public ResponseEntity<List<FormaPagamentoResponse>> listarTodas() {
        List<FormaPagamentoResponse> formas = formaPagamentoService.listarTodas();
        return ResponseEntity.ok(formas);
    }

    /**
     * Remove uma forma de pagamento pelo seu ID.
     *
     * @param id Identificador da forma.
     * @return Resposta 204 (sem conteúdo).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFormaPagamento(@PathVariable Long id) {
        formaPagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista todas as formas de pagamento ativas.
     *
     * @return Lista de formas de pagamento com o campo "ativo" igual a true.
     */
    @GetMapping("/ativas")
    public ResponseEntity<List<FormaPagamentoResponse>> listarAtivas() {
        List<FormaPagamentoResponse> formas = formaPagamentoService.listarAtivas();
        return ResponseEntity.ok(formas);
    }

    /**
     * Lista formas de pagamento de um tipo específico.
     *
     * @param tipo Tipo da forma de pagamento (PIX, CARTAO, DINHEIRO, etc.).
     * @return Lista de formas de pagamento do tipo informado.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<FormaPagamentoResponse>> listarPorTipo(@PathVariable TipoFormaPagamento tipo) {
        List<FormaPagamentoResponse> formas = formaPagamentoService.listarPorTipo(tipo);
        return ResponseEntity.ok(formas);
    }

    /**
     * Busca formas de pagamento cuja descrição contenha o termo informado.
     *
     * @param descricao Termo parcial da descrição.
     * @return Lista de formas de pagamento correspondentes.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<FormaPagamentoResponse>> buscarPorDescricao(@RequestParam String descricao) {
        List<FormaPagamentoResponse> formas = formaPagamentoService.buscarPorDescricao(descricao);
        return ResponseEntity.ok(formas);
    }

    // ==================================
    // 💼 OPERAÇÕES DE NEGÓCIO
    // ==================================

    /**
     * Ativa uma forma de pagamento.
     *
     * @param id ID da forma de pagamento.
     * @return FormaPagamentoResponse com status atualizado.
     */
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<FormaPagamentoResponse> ativarFormaPagamento(@PathVariable Long id) {
        FormaPagamentoResponse response = formaPagamentoService.ativar(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Inativa uma forma de pagamento.
     *
     * @param id ID da forma de pagamento.
     * @return FormaPagamentoResponse com status atualizado.
     */
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<FormaPagamentoResponse> inativarFormaPagamento(@PathVariable Long id) {
        FormaPagamentoResponse response = formaPagamentoService.inativar(id);
        return ResponseEntity.ok(response);
    }
}
