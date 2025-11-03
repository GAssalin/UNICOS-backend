package br.com.unicos.ms_pagamento.dto;

import br.com.unicos.ms_pagamento.enums.StatusPagamento;
import br.com.unicos.ms_pagamento.enums.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Record que representa os dados de entrada para criação ou atualização de um pagamento.
 */
public record PagamentoRequest(

        @NotNull(message = "O valor do pagamento é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "A data de vencimento é obrigatória.")
        LocalDate dataVencimento,

        LocalDate dataPagamento,

        @NotNull(message = "O status do pagamento é obrigatório.")
        StatusPagamento status,

        @NotNull(message = "O tipo de transação é obrigatório.")
        TipoTransacao tipoTransacao,

        String referenciaId,

        String observacao,

        @NotNull(message = "A forma de pagamento é obrigatória.")
        Long formaPagamentoId
) { }