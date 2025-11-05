package br.com.unicos.ms_estoque.dto;

import java.time.LocalDate;

/**
 * DTO de retorno com os dados completos de um lote ou série.
 */
public record LoteSerieResponse(
        Long id,
        String codigo,
        LocalDate dataValidade,
        String observacao
) {}
