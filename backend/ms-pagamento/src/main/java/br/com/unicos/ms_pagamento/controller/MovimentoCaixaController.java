package br.com.unicos.ms_pagamento.controller;

import br.com.unicos.ms_pagamento.dto.MovimentoCaixaRequest;
import br.com.unicos.ms_pagamento.dto.MovimentoCaixaResponse;
import br.com.unicos.ms_pagamento.enums.TipoMovimentoCaixa;
import br.com.unicos.ms_pagamento.service.MovimentoCaixaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST responsável pelo gerenciamento dos movimentos de caixa.
 *
 * Fornece endpoints para operações de CRUD, consultas específicas e
 * cálculos de totais financeiros de entradas e saídas.
 */
@RestController
@RequestMapping("/v1/movimentos-caixa")
public class MovimentoCaixaController {

    private final MovimentoCaixaService movimentoCaixaService;

    public MovimentoCaixaController(MovimentoCaixaService movimentoCaixaService) {
        this.movimentoCaixaService = movimentoCaixaService;
    }

    // ==================================
    // 🔹 CRUD
    // ==================================

    /**
     * Registra um novo movimento de caixa.
     *
     * @param request Dados do movimento.
     * @return MovimentoCaixaResponse com os dados do movimento criado.
     */
    @PostMapping
    public ResponseEntity<MovimentoCaixaResponse> criarMovimento(@Valid @RequestBody MovimentoCaixaRequest request) {
        MovimentoCaixaResponse response = movimentoCaixaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza os dados de um movimento de caixa existente.
     *
     * @param id      Identificador do movimento.
     * @param request Dados atualizados.
     * @return MovimentoCaixaResponse atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MovimentoCaixaResponse> atualizarMovimento(@PathVariable Long id,
                                                                     @Valid @RequestBody MovimentoCaixaRequest request) {
        MovimentoCaixaResponse response = movimentoCaixaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um movimento de caixa pelo seu ID.
     *
     * @param id Identificador do movimento.
     * @return MovimentoCaixaResponse encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovimentoCaixaResponse> buscarPorId(@PathVariable Long id) {
        return movimentoCaixaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os movimentos de caixa cadastrados.
     *
     * @return Lista de MovimentoCaixaResponse.
     */
    @GetMapping
    public ResponseEntity<List<MovimentoCaixaResponse>> listarTodos() {
        List<MovimentoCaixaResponse> movimentos = movimentoCaixaService.listarTodos();
        return ResponseEntity.ok(movimentos);
    }

    /**
     * Remove um movimento de caixa pelo seu ID.
     *
     * @param id Identificador do movimento.
     * @return Resposta 204 (sem conteúdo).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMovimento(@PathVariable Long id) {
        movimentoCaixaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==================================

    /**
     * Lista todos os movimentos de um determinado tipo (ENTRADA ou SAÍDA).
     *
     * @param tipoMovimento Tipo do movimento.
     * @return Lista de movimentos do tipo informado.
     */
    @GetMapping("/tipo/{tipoMovimento}")
    public ResponseEntity<List<MovimentoCaixaResponse>> listarPorTipo(@PathVariable TipoMovimentoCaixa tipoMovimento) {
        List<MovimentoCaixaResponse> movimentos = movimentoCaixaService.listarPorTipo(tipoMovimento);
        return ResponseEntity.ok(movimentos);
    }

    /**
     * Lista movimentos realizados em uma data específica.
     *
     * @param data Data do movimento.
     * @return Lista de movimentos do dia informado.
     */
    @GetMapping("/data")
    public ResponseEntity<List<MovimentoCaixaResponse>> listarPorData(@RequestParam LocalDate data) {
        List<MovimentoCaixaResponse> movimentos = movimentoCaixaService.listarPorData(data);
        return ResponseEntity.ok(movimentos);
    }

    // ==================================
    // 💼 OPERAÇÕES FINANCEIRAS
    // ==================================

    /**
     * Calcula o valor total de todas as entradas registradas.
     *
     * @return Valor total de entradas.
     */
    @GetMapping("/total/entradas")
    public ResponseEntity<BigDecimal> calcularTotalEntradas() {
        BigDecimal total = movimentoCaixaService.calcularTotalEntradas();
        return ResponseEntity.ok(total);
    }

    /**
     * Calcula o valor total de todas as saídas registradas.
     *
     * @return Valor total de saídas.
     */
    @GetMapping("/total/saidas")
    public ResponseEntity<BigDecimal> calcularTotalSaidas() {
        BigDecimal total = movimentoCaixaService.calcularTotalSaidas();
        return ResponseEntity.ok(total);
    }

    /**
     * Calcula o total de entradas realizadas no dia atual.
     *
     * @return Valor total de entradas de hoje.
     */
    @GetMapping("/hoje/entradas")
    public ResponseEntity<BigDecimal> totalEntradasHoje() {
        BigDecimal total = movimentoCaixaService.totalEntradasHoje();
        return ResponseEntity.ok(total);
    }

    /**
     * Calcula o total de saídas realizadas no dia atual.
     *
     * @return Valor total de saídas de hoje.
     */
    @GetMapping("/hoje/saidas")
    public ResponseEntity<BigDecimal> totalSaidasHoje() {
        BigDecimal total = movimentoCaixaService.totalSaidasHoje();
        return ResponseEntity.ok(total);
    }
}
