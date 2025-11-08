package br.com.unicos.ms_pagamento.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Client Feign responsável por consumir os endpoints do core-financeiro
 * relacionados às formas de pagamento.
 *
 * <p>
 * Utilizado pelo ms-pagamento para obter informações atualizadas sobre
 * métodos aceitos, status e descrições das formas de pagamento cadastradas.
 */
@FeignClient(name = "core-financeiro", path = "/v1/formas-pagamento")
public interface FormaPagamentoClient {

    /**
     * Lista todas as formas de pagamento disponíveis.
     *
     * @return Lista de formas de pagamento.
     */
    @GetMapping
    List<FormaPagamentoResponse> listarFormasPagamento();

    /**
     * Busca uma forma de pagamento pelo ID.
     *
     * @param id ID da forma de pagamento.
     * @return Detalhes da forma de pagamento correspondente.
     */
    @GetMapping("/{id}")
    FormaPagamentoResponse buscarPorId(@PathVariable Long id);

    /**
     * Lista as formas de pagamento filtradas por tipo.
     *
     * @param tipo Tipo da forma (PIX, CARTAO, BOLETO, etc).
     * @return Lista de formas de pagamento do tipo informado.
     */
    @GetMapping("/tipo")
    List<FormaPagamentoResponse> listarPorTipo(@RequestParam("tipo") String tipo);
}
