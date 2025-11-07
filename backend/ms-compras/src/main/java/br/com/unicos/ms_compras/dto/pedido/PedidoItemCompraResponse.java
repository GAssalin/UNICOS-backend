package br.com.unicos.ms_compras.dto.pedido;

import java.math.BigDecimal;

/**
 * DTO de resposta para itens do pedido de compra.
 *
 * <p>
 * Retorna informações detalhadas do item, incluindo valores calculados.
 * </p>
 */
public record PedidoItemCompraResponse(

        Long id,
        Long produtoId,
        BigDecimal quantidade,
        BigDecimal precoUnitario,
        BigDecimal desconto,
        BigDecimal valorTotal,
        Long pedidoCompraId
) { }
