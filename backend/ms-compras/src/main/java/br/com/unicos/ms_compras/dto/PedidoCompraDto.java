package br.com.unicos.ms_compras.dto;

import br.com.unicos.ms_compras.enums.MotivoCancelamentoCompra;
import br.com.unicos.ms_compras.enums.StatusPedidoCompra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de pedido de compra.
 */
public record PedidoCompraDto(
        Long id,
        String codigo,
        Long fornecedorId,
        LocalDate dataEmissao,
        LocalDate dataPrevistaEntrega,
        StatusPedidoCompra statusPedidoCompra,
        Long condicaoPagamentoId,
        String observacao,
        BigDecimal subtotal,
        BigDecimal desconto,
        BigDecimal frete,
        BigDecimal total,
        Long aprovadoPor,
        LocalDateTime aprovadoEm,
        MotivoCancelamentoCompra motivoCancelamentoCompra,
        String observacaoCancelamento,
        Long canceladoPor,
        LocalDateTime canceladoEm
) { }
