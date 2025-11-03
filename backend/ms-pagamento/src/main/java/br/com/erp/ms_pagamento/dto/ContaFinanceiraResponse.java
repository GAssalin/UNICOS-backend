package br.com.erp.ms_pagamento.dto;

import br.com.erp.ms_pagamento.enums.TipoConta;

import java.math.BigDecimal;

/**
 * Record que representa os dados de retorno de uma conta financeira.
 */
public record ContaFinanceiraResponse(
        Long id,
        String descricao,
        String banco,
        String agencia,
        String numeroConta,
        TipoConta tipoConta,
        BigDecimal saldoAtual
) { }