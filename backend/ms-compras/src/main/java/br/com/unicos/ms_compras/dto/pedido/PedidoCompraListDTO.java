package br.com.unicos.ms_compras.dto.pedido;

import br.com.unicos.ms_compras.enums.StatusPedidoCompra;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO utilizado para listagem de pedidos de compra.
 *
 * <p>Fornece uma visão resumida dos pedidos para exibição em tabelas e listagens.</p>
 */
public record PedidoCompraListDTO(

        Long id,
        Long fornecedorId,
        StatusPedidoCompra status,
        LocalDateTime dataCriacao,
        BigDecimal valorTotal
) { }
