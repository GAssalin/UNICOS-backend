package br.com.unicos.ms_pedido.enums;

/**
 * Enum que representa o status atual de um pedido.
 *
 * Indica em qual etapa do fluxo de processamento o pedido se encontra.
 */
public enum StatusPedido {

    /**
     * Pedido criado, mas ainda não finalizado ou pago.
     */
    ABERTO("Aberto"),

    /**
     * Pedido pago e aguardando envio ou entrega.
     */
    PAGO("Pago"),

    /**
     * Pedido em transporte ou a caminho do cliente.
     */
    ENVIADO("Enviado"),

    /**
     * Pedido cancelado por solicitação do cliente ou por falha no processo.
     */
    CANCELADO("Cancelado"),

    /**
     * Pedido concluído com sucesso e entregue ao cliente.
     */
    FINALIZADO("Finalizado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
