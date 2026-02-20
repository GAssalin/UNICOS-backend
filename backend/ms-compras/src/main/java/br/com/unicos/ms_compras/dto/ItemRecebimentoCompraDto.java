package br.com.unicos.ms_compras.dto;

import java.math.BigDecimal;

/**
 * DTO de item do recebimento de compra.
 */
public record ItemRecebimentoCompraDto(
        Long id,
        Long recebimentoCompraId,
        Long itemPedidoCompraId,
        BigDecimal quantidadeRecebida,
        BigDecimal quantidadeAprovada,
        BigDecimal quantidadeRecusada,
        String observacao
) {
}
