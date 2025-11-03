package br.com.unicos.ms_pagamento.dto;

import br.com.unicos.ms_pagamento.enums.TipoFormaPagamento;

/**
 * Record que representa os dados de saída de uma forma de pagamento.
 */
public record FormaPagamentoResponse(
        Long id,
        String descricao,
        TipoFormaPagamento tipo,
        Boolean ativo
) { }