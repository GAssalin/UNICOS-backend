package br.com.unicos.ms_pagamento.dto;

import br.com.unicos.ms_pagamento.enums.StatusParcela;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Record que representa os dados de entrada para criação ou atualização de uma parcela de pagamento.
 */
public record ParcelaPagamentoRequest(

        @NotNull(message = "O número da parcela é obrigatório.")
        Integer numeroParcela,

        @NotNull(message = "O valor da parcela é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O valor da parcela deve ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "A data de vencimento é obrigatória.")
        LocalDate dataVencimento,

        LocalDate dataPagamento,

        @NotNull(message = "O status da parcela é obrigatório.")
        StatusParcela status,

        @NotNull(message = "O ID do pagamento é obrigatório.")
        Long pagamentoId
) { }