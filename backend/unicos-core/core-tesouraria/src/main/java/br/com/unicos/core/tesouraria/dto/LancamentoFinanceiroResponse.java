package br.com.unicos.core.tesouraria.dto;

import br.com.unicos.core.tesouraria.enums.TipoLancamentoFinanceiro;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta contendo os dados completos de um lançamento financeiro.
 */
public record LancamentoFinanceiroResponse(
        Long id,
        Long contaFinanceiraId,
        TipoLancamentoFinanceiro tipoLancamento,
        BigDecimal valorBruto,
        BigDecimal valorLiquido,
        String descricao,
        LocalDate dataLancamento,
        LocalDate dataCompetencia,
        String referenciaOrigem,
        String origemSistema
) { }
