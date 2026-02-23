package br.com.unicos.ms_compras.dto;

import java.time.LocalDate;

/**
 * DTO de recebimento de compra.
 */
public record RecebimentoCompraDto(
        Long id,
        Long pedidoCompraId,
        Long fornecedorId,
        LocalDate dataRecebimento,
        String status,
        String observacao
) { }
