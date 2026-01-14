package br.com.unicos.ms_filial.dto.status;

import br.com.unicos.ms_filial.enums.StatusFilial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO de criação de FilialStatusHistorico.
 */
public record FilialStatusHistoricoCreateRequest(
        @NotNull Long filialId,
        StatusFilial statusAnterior,
        @NotNull StatusFilial statusNovo,
        @NotNull LocalDateTime dataAlteracao,
        @NotBlank String motivo,
        Long usuarioId
) { }
