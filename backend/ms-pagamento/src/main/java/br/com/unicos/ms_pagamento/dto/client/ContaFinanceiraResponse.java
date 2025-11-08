package br.com.unicos.ms_pagamento.dto.client;

import java.math.BigDecimal;

/**
 * DTO de resposta para representar uma Conta Financeira
 * recebida do microserviço core-financeiro.
 */
public record ContaFinanceiraResponse(
        Long id,
        String descricao,
        String tipoConta,
        BigDecimal saldoAtual,
        boolean ativo
) {}
