package br.com.erp.ms_pagamento.dto;

import br.com.erp.ms_pagamento.enums.StatusTransacao;
import br.com.erp.ms_pagamento.enums.TipoFormaPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Record que representa os dados de saída de uma transação financeira.
 */
public record TransacaoFinanceiraResponse(
        Long id,
        String codigoTransacao,
        LocalDateTime dataTransacao,
        BigDecimal valor,
        StatusTransacao status,
        TipoFormaPagamento tipo
) { }