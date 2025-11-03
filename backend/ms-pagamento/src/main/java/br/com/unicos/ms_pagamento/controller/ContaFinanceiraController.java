package br.com.unicos.ms_pagamento.controller;

import br.com.unicos.ms_pagamento.dto.ContaFinanceiraRequest;
import br.com.unicos.ms_pagamento.dto.ContaFinanceiraResponse;
import br.com.unicos.ms_pagamento.enums.TipoConta;
import br.com.unicos.ms_pagamento.service.ContaFinanceiraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento das contas financeiras.
 *
 * Fornece endpoints para operações de CRUD, consultas específicas
 * e cálculos de saldo consolidado e análise de contas.
 */
@RestController
@RequestMapping("/v1/contas-financeiras")
public class ContaFinanceiraController {

    private final ContaFinanceiraService contaFinanceiraService;

    public ContaFinanceiraController(ContaFinanceiraService contaFinanceiraService) {
        this.contaFinanceiraService = contaFinanceiraService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Cadastra uma nova conta financeira.
     *
     * @param request Dados da conta a ser criada.
     * @return ContaFinanceiraResponse com os dados da conta criada.
     */
    @PostMapping
    public ResponseEntity<ContaFinanceiraResponse> criarConta(@Valid @RequestBody ContaFinanceiraRequest request) {
        ContaFinanceiraResponse response = contaFinanceiraService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de uma conta financeira existente.
     *
     * @param id      Identificador da conta financeira.
     * @param request Dados atualizados da conta.
     * @return ContaFinanceiraResponse atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContaFinanceiraResponse> atualizarConta(@PathVariable Long id,
                                                                  @Valid @RequestBody ContaFinanceiraRequest request) {
        ContaFinanceiraResponse response = contaFinanceiraService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma conta financeira pelo seu ID.
     *
     * @param id Identificador da conta financeira.
     * @return ContaFinanceiraResponse encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContaFinanceiraResponse> buscarPorId(@PathVariable Long id) {
        return contaFinanceiraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as contas financeiras cadastradas.
     *
     * @return Lista de ContaFinanceiraResponse.
     */
    @GetMapping
    public ResponseEntity<List<ContaFinanceiraResponse>> listarTodas() {
        List<ContaFinanceiraResponse> contas = contaFinanceiraService.listarTodas();
        return ResponseEntity.ok(contas);
    }

    /**
     * Remove uma conta financeira pelo seu ID.
     *
     * @param id Identificador da conta.
     * @return Resposta 204 (sem conteúdo).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id) {
        contaFinanceiraService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista todas as contas financeiras de um tipo específico.
     *
     * @param tipoConta Tipo da conta (CAIXA, CONTA_CORRENTE, CONTA_POUPANCA).
     * @return Lista de contas do tipo informado.
     */
    @GetMapping("/tipo/{tipoConta}")
    public ResponseEntity<List<ContaFinanceiraResponse>> listarPorTipo(@PathVariable TipoConta tipoConta) {
        List<ContaFinanceiraResponse> contas = contaFinanceiraService.listarPorTipo(tipoConta);
        return ResponseEntity.ok(contas);
    }

    /**
     * Busca contas cuja descrição contenha o termo informado.
     *
     * @param descricao Termo de busca parcial.
     * @return Lista de contas com descrição correspondente.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ContaFinanceiraResponse>> buscarPorDescricao(@RequestParam String descricao) {
        List<ContaFinanceiraResponse> contas = contaFinanceiraService.buscarPorDescricao(descricao);
        return ResponseEntity.ok(contas);
    }

    // ==================================
    // 💼 OPERAÇÕES FINANCEIRAS
    // ==================================

    /**
     * Calcula o saldo total consolidado de todas as contas financeiras.
     *
     * @return Valor total somado dos saldos das contas.
     */
    @GetMapping("/saldo/total")
    public ResponseEntity<BigDecimal> calcularSaldoTotal() {
        BigDecimal saldo = contaFinanceiraService.calcularSaldoTotal();
        return ResponseEntity.ok(saldo);
    }

    /**
     * Conta quantas contas estão com saldo negativo.
     *
     * @return Quantidade de contas com saldo abaixo de zero.
     */
    @GetMapping("/saldo/negativas")
    public ResponseEntity<Long> contarContasNegativas() {
        Long quantidade = contaFinanceiraService.contarContasNegativas();
        return ResponseEntity.ok(quantidade);
    }

    /**
     * Atualiza o saldo de uma conta financeira.
     *
     * @param id    ID da conta financeira.
     * @param valor Novo valor do saldo.
     * @return ContaFinanceiraResponse com saldo atualizado.
     */
    @PatchMapping("/{id}/saldo")
    public ResponseEntity<ContaFinanceiraResponse> atualizarSaldo(@PathVariable Long id,
                                                                  @RequestParam BigDecimal valor) {
        ContaFinanceiraResponse response = contaFinanceiraService.atualizarSaldo(id, valor);
        return ResponseEntity.ok(response);
    }
}
