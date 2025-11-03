package br.com.erp.ms_pagamento.dto;

import br.com.erp.ms_pagamento.enums.StatusTransacao;
import br.com.erp.ms_pagamento.enums.TipoFormaPagamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Record que representa os dados de entrada para criação ou atualização de uma transação financeira.
 */
public record TransacaoFinanceiraRequest(

        String codigoTransacao,

        @NotNull(message = "A data da transação é obrigatória.")
        LocalDateTime dataTransacao,

        @NotNull(message = "O valor da transação é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O valor da transação deve ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "O status da transação é obrigatório.")
        StatusTransacao status,

        @NotNull(message = "O tipo da transação é obrigatório.")
        TipoFormaPagamento tipo,

        @NotNull(message = "O ID do pagamento é obrigatório.")
        Long pagamentoId
) { }