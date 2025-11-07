package br.com.unicos.ms_compras.dto.pedido;

import java.math.BigDecimal;

/**
 * DTO utilizado para listagem resumida de itens do pedido de compra.
 *
 * <p>
 * Ideal para exibição em tabelas e consultas rápidas.
 * </p>
 */
public record PedidoItemCompraListDTO(

        Long id,
        Long produtoId,
        BigDecimal quantidade,
        BigDecimal precoUnitario,
        BigDecimal valorTotal
) { }
