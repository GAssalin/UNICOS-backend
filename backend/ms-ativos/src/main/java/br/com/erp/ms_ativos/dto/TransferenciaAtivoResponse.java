package br.com.erp.ms_ativos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaAtivoResponse {

    private Long id;
    private Long ativoId;
    private Long origemId;
    private Long destinoId;
    private LocalDate dataTransferencia;
    private String motivo;
}