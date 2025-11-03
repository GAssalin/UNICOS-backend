package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.StatusManutencao;
import br.com.unicos.ms_ativos.enums.TipoManutencao;

import java.time.LocalDate;

/**
 * DTO usado para listagem simplificada de manutenções.
 */
public record ManutencaoAtivoListDTO(
        Long id,
        Long ativoId,
        LocalDate dataManutencao,
        TipoManutencao tipo,
        StatusManutencao status
) {}