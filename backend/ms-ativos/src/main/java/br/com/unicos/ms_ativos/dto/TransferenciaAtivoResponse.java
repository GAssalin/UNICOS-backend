package br.com.unicos.ms_ativos.dto;

import java.time.LocalDate;

/**
 * DTO usado para retorno detalhado das informações de uma transferência de ativo.
 */
public record TransferenciaAtivoResponse(
        Long id,
        Long ativoId,
        Long origemId,
        Long destinoId,
        LocalDate dataTransferencia,
        String motivo
) {}