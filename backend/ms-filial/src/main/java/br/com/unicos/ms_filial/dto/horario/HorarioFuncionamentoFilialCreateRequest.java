package br.com.unicos.ms_filial.dto.horario;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO de criação de HorarioFuncionamentoFilial.
 */
public record HorarioFuncionamentoFilialCreateRequest(
        @NotNull Long filialId,
        @NotNull DayOfWeek diaSemana,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        @NotNull Boolean aberto
) { }
