package br.com.unicos.ms_compras.dto.pedido;

import br.com.unicos.ms_compras.enums.StatusPedidoCompra;

import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * DTO de resposta detalhada do pedido de compra.
 *
 * <p>Inclui os dados do pedido e as informações herdadas do módulo core.</p>
 */
public record PedidoCompraResponse(

        Long id,
        Long fornecedorId,
        StatusPedidoCompra status,
        String observacao,

        /**
         * Campos herdados do {@link br.com.unicos.core.pedido.model.Pedido}.
         */
        String codigo,
        LocalDate dataCriacao,
        LocalDate dataConclusao,
        BigDecimal valorTotal,
        BigDecimal valorDescontos,
        BigDecimal valorLiquido
) {}
