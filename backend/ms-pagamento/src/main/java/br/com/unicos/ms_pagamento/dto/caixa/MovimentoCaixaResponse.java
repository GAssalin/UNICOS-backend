package br.com.unicos.ms_pagamento.dto.caixa;

import br.com.unicos.ms_pagamento.enums.TipoMovimentoCaixa;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Record que representa os dados de retorno de um movimento de caixa.
 */
public record MovimentoCaixaResponse(
        Long id,
        LocalDate dataMovimento,
        BigDecimal valor,
        TipoMovimentoCaixa tipoMovimento,
        String descricao,
        ContaFinanceiraResponse contaFinanceira
) { }