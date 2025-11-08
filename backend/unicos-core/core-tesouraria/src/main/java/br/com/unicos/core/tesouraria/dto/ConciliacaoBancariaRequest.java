package br.com.unicos.core.tesouraria.dto;

import br.com.unicos.core.tesouraria.enums.StatusConciliacao;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para criação ou atualização de uma conciliação bancária.
 */
public record ConciliacaoBancariaRequest(
        @NotNull Long contaFinanceiraId,
        Long lancamentoFinanceiroId,
        @NotNull StatusConciliacao status,
        @NotNull LocalDate dataConciliacao,
        BigDecimal valorConciliado,
        BigDecimal diferenca,
        String observacao
) { }
