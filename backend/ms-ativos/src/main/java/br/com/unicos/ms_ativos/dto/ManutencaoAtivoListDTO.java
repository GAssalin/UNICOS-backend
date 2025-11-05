package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.StatusManutencao;
import br.com.unicos.ms_ativos.enums.TipoManutencao;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO simplificado para listagem de manutenções de ativos.
 * <p>
 * Usado em consultas de tabela, relatórios e dashboards.
 */
public record ManutencaoAtivoListDTO(

        /** Identificador único da manutenção. */
        Long id,

        /** Nome do ativo. */
        String nomeAtivo,

        /** Tipo da manutenção (PREVENTIVA ou CORRETIVA). */
        TipoManutencao tipo,

        /** Data da manutenção. */
        LocalDate dataManutencao,

        /** Status atual da manutenção. */
        StatusManutencao status,

        /** Custo total da manutenção. */
        BigDecimal custo
) { }
