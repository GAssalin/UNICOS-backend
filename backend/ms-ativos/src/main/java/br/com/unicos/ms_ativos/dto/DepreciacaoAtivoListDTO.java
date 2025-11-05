package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.TipoDepreciacao;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO simplificado para listagem de depreciações.
 * <p>
 * Usado em consultas resumidas, dashboards e relatórios contábeis.
 */
public record DepreciacaoAtivoListDTO(

        /** Identificador único da depreciação. */
        Long id,

        /** Tipo da depreciação. */
        TipoDepreciacao tipo,

        /** Data da competência contábil. */
        LocalDate dataCompetencia,

        /** Valor depreciado no período. */
        BigDecimal valorDepreciado,

        /** Saldo contábil resultante. */
        BigDecimal saldoContabil
) { }
