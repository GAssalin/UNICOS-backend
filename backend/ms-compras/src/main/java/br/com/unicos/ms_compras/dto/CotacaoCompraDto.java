package br.com.unicos.ms_compras.dto;

import java.time.LocalDate;

/**
 * DTO de cotação de compra.
 */
public record CotacaoCompraDto(
        Long id,
        String codigo,
        LocalDate dataAbertura,
        LocalDate dataValidade,
        String status,
        String observacao,
        Long pedidoCompraId
) { }
