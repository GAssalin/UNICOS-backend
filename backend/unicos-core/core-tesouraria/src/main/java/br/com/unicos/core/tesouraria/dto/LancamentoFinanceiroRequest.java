package br.com.unicos.core.tesouraria.dto;

import br.com.unicos.core.tesouraria.enums.TipoLancamentoFinanceiro;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para criação ou atualização de um lançamento financeiro.
 */
public record LancamentoFinanceiroRequest(
        @NotNull Long contaFinanceiraId,
        @NotNull TipoLancamentoFinanceiro tipoLancamento,
        @NotNull BigDecimal valorBruto,
        BigDecimal valorLiquido,
        String descricao,
        @NotNull LocalDate dataLancamento,
        LocalDate dataCompetencia,
        String referenciaOrigem,
        String origemSistema
) { }
