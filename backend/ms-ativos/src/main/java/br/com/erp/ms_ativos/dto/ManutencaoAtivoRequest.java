package br.com.erp.ms_ativos.dto;

import br.com.erp.ms_ativos.enums.StatusManutencao;
import br.com.erp.ms_ativos.enums.TipoManutencao;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO usado para criação e atualização de manutenções de ativo.
 */
public record ManutencaoAtivoRequest(
        @NotNull
        Long ativoId,

        @NotNull @PastOrPresent
        LocalDate dataManutencao,

        @NotNull
        TipoManutencao tipo,

        @Size(max = 255)
        String descricaoServico,

        @DecimalMin("0.0")
        BigDecimal custo,

        @NotNull
        StatusManutencao status
) {}