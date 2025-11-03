package br.com.unicos.ms_pedido.enums;

import lombok.Getter;

/**
 * Enum que representa o status logístico da entrega de um pedido.
 *
 * Indica o andamento do processo de envio até a entrega ao cliente.
 */
@Getter
public enum StatusEntrega {

    /**
     * Aguardando início do envio.
     */
    AGUARDANDO("Aguardando"),

    /**
     * Pedido em transporte para o cliente.
     */
    EM_TRANSITO("Em trânsito"),

    /**
     * Pedido entregue ao cliente.
     */
    ENTREGUE("Entregue"),

    /**
     * Pedido devolvido por algum motivo logístico.
     */
    DEVOLVIDO("Devolvido");

    private final String descricao;

    StatusEntrega(String descricao) {
        this.descricao = descricao;
    }

}
