package br.com.unicos.core.pedido.enums;

import lombok.Getter;

/**
 * Enum que define o tipo de origem do pedido de compra.
 * <p>
 * Usado para classificar como o pedido foi originado no processo de compras,
 * podendo também abranger canais de venda quando aplicável.
 */
@Getter
public enum TipoPedido {

    /**
     * Pedido originado a partir de uma requisição interna.
     */
    REQUISICAO_INTERNA("Requisição interna"),

    /**
     * Pedido gerado após uma cotação aprovada.
     */
    COTACAO("Cotação aprovada"),

    /**
     * Pedido criado com base em contrato vigente com o fornecedor.
     */
    CONTRATO("Pedido gerado por contrato"),

    /**
     * Pedido de compra feito em caráter emergencial.
     */
    URGENCIA("Compra emergencial"),

    /**
     * Pedido realizado por meio de plataforma online (e-commerce ou portal interno).
     */
    ONLINE("Online"),

    /**
     * Pedido feito presencialmente no ponto de venda ou balcão.
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
