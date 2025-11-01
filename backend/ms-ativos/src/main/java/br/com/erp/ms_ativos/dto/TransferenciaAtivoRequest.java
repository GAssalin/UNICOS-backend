package br.com.erp.ms_ativos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaAtivoRequest {

    @NotNull
    private Long ativoId;

    @NotNull
    private Long origemId;

    @NotNull
    private Long destinoId;

    @NotNull
    @PastOrPresent
    private LocalDate dataTransferencia;

    @Size(max = 255)
    private String motivo;
}