package br.com.unicos.ms_filial.dto.status;

import br.com.unicos.ms_filial.enums.StatusFilial;

import java.time.LocalDateTime;

/**
 * DTO de resposta de FilialStatusHistorico.
 */
public record FilialStatusHistoricoResponse(
        Long id,
        Long filialId,
        StatusFilial statusAnterior,
        StatusFilial statusNovo,
        LocalDateTime dataAlteracao,
        String motivo,
        Long usuarioId
) { }
