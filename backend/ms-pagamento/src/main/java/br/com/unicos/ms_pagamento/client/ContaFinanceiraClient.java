package br.com.unicos.ms_pagamento.client;

import br.com.unicos.ms_pagamento.dto.client.ContaFinanceiraResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Client Feign responsável por consumir os endpoints do microserviço core-financeiro
 * relacionados à entidade ContaFinanceira.
 *
 * <p>
 * Fornece métodos para consulta de contas financeiras, permitindo que o ms-pagamento
 * acesse saldos e tipos de contas de forma centralizada e desacoplada.
 */
@FeignClient(name = "core-financeiro", path = "/v1/contas-financeiras")
public interface ContaFinanceiraClient {

    /**
     * Lista todas as contas financeiras ativas.
     *
     * @return Lista de contas financeiras.
     */
    @GetMapping
    List<ContaFinanceiraResponse> listarContas();

    /**
     * Busca uma conta financeira pelo ID.
     *
     * @param id ID da conta financeira.
     * @return Detalhes da conta financeira correspondente.
     */
    @GetMapping("/{id}")
    ContaFinanceiraResponse buscarPorId(@PathVariable Long id);

    /**
     * Lista as contas financeiras filtradas por tipo.
     *
     * @param tipoConta Tipo da conta (CONTA_CORRENTE, CONTA_POUPANCA, CAIXA, etc).
     * @return Lista de contas do tipo informado.
     */
    @GetMapping("/tipo")
    List<ContaFinanceiraResponse> listarPorTipo(@RequestParam("tipoConta") String tipoConta);
}
