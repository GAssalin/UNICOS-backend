package br.com.unicos.ms_pagamento.dto.gateway;

import br.com.unicos.ms_pagamento.enums.StatusTransacao;
import br.com.unicos.ms_pagamento.enums.TipoFormaPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Record que representa os dados de saída de uma transação financeira.
 */
public record TransacaoPagamentoResponse(
        Long id,
        String codigoTransacao,
        LocalDateTime dataTransacao,
        BigDecimal valor,
        StatusTransacao status,
        TipoFormaPagamento tipo
) { }