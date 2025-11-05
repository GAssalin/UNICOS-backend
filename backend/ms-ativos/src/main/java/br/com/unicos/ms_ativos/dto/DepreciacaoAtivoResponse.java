package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.TipoDepreciacao;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta utilizado para exibição detalhada
 * das informações de depreciação aplicadas a um ativo.
 */
public record DepreciacaoAtivoResponse(

        /** Identificador único da depreciação. */
        Long id,

        /** Identificador do ativo vinculado à depreciação. */
        Long ativoId,

        /** Nome do ativo, para exibição em relatórios. */
        String nomeAtivo,

        /** Tipo da depreciação. */
        TipoDepreciacao tipo,

        /** Data da competência contábil da depreciação. */
        LocalDate dataCompetencia,

        /** Valor depreciado no período. */
        BigDecimal valorDepreciado,

        /** Saldo contábil após a depreciação. */
        BigDecimal saldoContabil
) { }
