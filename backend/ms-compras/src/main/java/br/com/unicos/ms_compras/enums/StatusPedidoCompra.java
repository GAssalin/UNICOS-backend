package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Enum que representa os estágios do pedido de compra.
 */
@Getter
public enum StatusPedidoCompra {

    /**
     * Pedido criado mas ainda não autorizado.
     */
    PENDENTE_APROVACAO("Pedido aguardando aprovação do gestor."),

    /**
     * Pedido aprovado e encaminhado ao fornecedor.
     */
    APROVADO("Pedido aprovado e enviado ao fornecedor."),

    /**
     * Pedido já entregue ou em processo de recebimento.
     */
    EM_RECEBIMENTO("Pedido com entrega em andamento."),

    /**
     * Pedido totalmente recebido e finalizado.
     */
    CONCLUIDO("Pedido recebido e encerrado."),

    /**
     * Pedido cancelado antes da conclusão.
     */
    CANCELADO("Pedido de compra cancelado.");

    private final String descricao;

    StatusPedidoCompra(String descricao) {
        this.descricao = descricao;
    }
}
