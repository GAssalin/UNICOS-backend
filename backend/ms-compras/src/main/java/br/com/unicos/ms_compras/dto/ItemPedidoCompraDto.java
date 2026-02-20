package br.com.unicos.ms_compras.dto;

import java.math.BigDecimal;

/**
 * DTO de item de pedido de compra.
 */
public record ItemPedidoCompraDto(
        Long id,
        Long pedidoCompraId,
        Long produtoId,
        String produtoDescricaoSnapshot,
        String unidadeSnapshot,
        BigDecimal quantidade,
        BigDecimal precoUnitario,
        BigDecimal descontoItem,
        BigDecimal totalItem,
        String observacao
) {
}
