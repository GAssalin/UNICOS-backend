package br.com.unicos.ms_pagamento.dto.core;

import br.com.unicos.ms_pagamento.enums.StatusParcela;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Record que representa os dados de retorno de uma parcela de pagamento.
 */
public record ParcelaPagamentoResponse(
        Long id,
        Integer numeroParcela,
        BigDecimal valor,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        StatusParcela status
) { }