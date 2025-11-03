package br.com.erp.ms_ativos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO usado para criação e atualização de transferências de ativo.
 */
public record TransferenciaAtivoRequest(
        @NotNull
        Long ativoId,

        @NotNull
        Long origemId,

        @NotNull
        Long destinoId,

        @NotNull @PastOrPresent
        LocalDate dataTransferencia,

        @Size(max = 255)
        String motivo
) {}