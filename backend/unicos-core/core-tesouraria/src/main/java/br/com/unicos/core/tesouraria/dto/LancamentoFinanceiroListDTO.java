package br.com.unicos.core.tesouraria.dto;

import br.com.unicos.core.tesouraria.enums.TipoLancamentoFinanceiro;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para listagem resumida de lançamentos financeiros.
 */
public record LancamentoFinanceiroListDTO(
        Long id,
        TipoLancamentoFinanceiro tipoLancamento,
        BigDecimal valorBruto,
        LocalDate dataLancamento,
        String descricao
) { }
