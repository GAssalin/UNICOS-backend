package br.com.erp.ms_ativos.dto;

import br.com.erp.ms_ativos.enums.StatusManutencao;
import br.com.erp.ms_ativos.enums.TipoManutencao;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO usado para retorno detalhado de manutenções de ativo.
 */
public record ManutencaoAtivoResponse(
        Long id,
        Long ativoId,
        LocalDate dataManutencao,
        TipoManutencao tipo,
        String descricaoServico,
        BigDecimal custo,
        StatusManutencao status
) {}