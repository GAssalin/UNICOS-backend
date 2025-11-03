package br.com.unicos.ms_ativos.dto;

import java.time.LocalDate;

/**
 * DTO usado para listagem simplificada das transferências de ativo.
 */
public record TransferenciaAtivoListDTO(
        Long id,
        Long ativoId,
        LocalDate dataTransferencia,
        Long origemId,
        Long destinoId
) {}