package br.com.unicos.ms_pedido.enums;

import lombok.Getter;

/**
 * Enum que define o tipo de origem do pedido.
 * <p>
 * Usado para classificar o canal de venda que originou o pedido.
 */
@Getter
public enum TipoPedido {

    /**
     * Pedido realizado por meio de plataforma online.
     */
    ONLINE("Online"),

    /**
     * Pedido feito presencialmente no ponto de venda.
     */
    PRESENCIAL("Presencial"),

    /**
     * Pedido realizado via telefone ou atendimento remoto.
     */
    TELEFONE("Telefone");

    private final String descricao;

    TipoPedido(String descricao) {
        this.descricao = descricao;
    }

}
