package br.com.unicos.core.tesouraria.dto;

import br.com.unicos.core.tesouraria.enums.StatusConciliacao;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta contendo os dados completos de uma conciliação bancária.
 */
public record ConciliacaoBancariaResponse(
        Long id,
        Long contaFinanceiraId,
        Long lancamentoFinanceiroId,
        StatusConciliacao status,
        LocalDate dataConciliacao,
        BigDecimal valorConciliado,
        BigDecimal diferenca,
        String observacao
) { }
