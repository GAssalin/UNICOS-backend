package br.com.unicos.core.pedido.enums;

import lombok.Getter;

@Getter
public enum StatusPedido implements StatusPedidoBase {
    /**
     * Pedido criado, aguardando processamento inicial.
     */
    ABERTO("Aberto"),

    /**
     * Pedido em andamento, sendo processado ou aguardando ação intermediária.
     */
    EM_ANDAMENTO("Em andamento"),

    /**
     * Pedido finalizado com sucesso.
     */
    FINALIZADO("Finalizado"),

    /**
     * Pedido cancelado, sem continuidade no fluxo.
     */
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }
}
