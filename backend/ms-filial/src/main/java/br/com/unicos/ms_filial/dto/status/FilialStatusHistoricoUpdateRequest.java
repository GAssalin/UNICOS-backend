package br.com.unicos.ms_filial.dto.status;

import br.com.unicos.ms_filial.enums.StatusFilial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO de atualização de FilialStatusHistorico.
 */
public record FilialStatusHistoricoUpdateRequest(
        @NotNull Long filialId,
        StatusFilial statusAnterior,
        @NotNull StatusFilial statusNovo,
        @NotNull LocalDateTime dataAlteracao,
        @NotBlank String motivo,
        Long usuarioId
) { }
