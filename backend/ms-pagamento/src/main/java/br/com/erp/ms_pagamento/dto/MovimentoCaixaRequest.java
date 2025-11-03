package br.com.erp.ms_pagamento.dto;

import br.com.erp.ms_pagamento.enums.TipoMovimentoCaixa;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Record que representa os dados de entrada para criação ou atualização de um movimento de caixa.
 */
public record MovimentoCaixaRequest(

        @NotNull(message = "A data do movimento é obrigatória.")
        LocalDate dataMovimento,

        @NotNull(message = "O valor do movimento é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "O tipo de movimento é obrigatório.")
        TipoMovimentoCaixa tipoMovimento,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao,

        @NotNull(message = "A conta financeira é obrigatória.")
        Long contaFinanceiraId,

        Long pagamentoId
) { }