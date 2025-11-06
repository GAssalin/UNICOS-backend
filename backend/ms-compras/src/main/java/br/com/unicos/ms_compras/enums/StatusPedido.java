package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Enum que representa o status atual de um pedido de compra.
 * <p>
 * Indica em qual etapa do fluxo de processamento o pedido se encontra.
 */
@Getter
public enum StatusPedido {

    /**
     * Pedido criado, mas ainda não aprovado.
     */
    ABERTO("Aberto"),

    /**
     * Pedido aprovado internamente para emissão ao fornecedor.
     */
    APROVADO("Aprovado"),

    /**
     * Pedido enviado ao fornecedor.
     */
    ENVIADO_FORNECEDOR("Enviado ao fornecedor"),

    /**
     * Pedido recebido parcial ou totalmente pela empresa.
     */
    RECEBIDO("Recebido"),

    /**
     * Pedido cancelado por solicitação, erro ou reprovação.
     */
    CANCELADO("Cancelado"),

    /**
     * Pedido pago e concluído.
     */
    FINALIZADO("Finalizado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }
}
