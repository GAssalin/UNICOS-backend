package br.com.unicos.ms_filial.dto.horario;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO de resposta de HorarioFuncionamentoFilial.
 */
public record HorarioFuncionamentoFilialResponse(
        Long id,
        Long filialId,
        DayOfWeek diaSemana,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        Boolean aberto
) { }
