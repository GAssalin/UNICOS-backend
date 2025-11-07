package br.com.unicos.ms_compras.dto.pedido;

import br.com.unicos.ms_compras.enums.StatusPedidoCompra;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de resposta detalhada do pedido de compra.
 *
 * <p>Inclui os dados específicos do pedido e as informações herdadas do módulo core.</p>
 */
public record PedidoCompraResponse(

        Long id,
        Long fornecedorId,
        StatusPedidoCompra status,
        String observacao,

        /**
         * Campos herdados do {@link br.com.unicos.core.pedido.model.Pedido}.
         */
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        BigDecimal valorTotal,

        /**
         * Itens vinculados ao pedido de compra.
         */
        List<PedidoItemCompraResponse> itens
) { }
