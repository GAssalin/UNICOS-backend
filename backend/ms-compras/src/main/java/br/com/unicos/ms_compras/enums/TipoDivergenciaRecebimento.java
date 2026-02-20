package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Tipos de divergência no recebimento.
 */
@Getter
public enum TipoDivergenciaRecebimento {

    /**
     * Quantidade recebida menor que a solicitada.
     */
    QUANTIDADE_MENOR("Quantidade menor que o pedido"),

    /**
     * Quantidade recebida maior que a solicitada.
     */
    QUANTIDADE_MAIOR("Quantidade maior que o pedido"),

    /**
     * Produto avariado.
     */
    AVARIA("Avaria"),

    /**
     * Produto diferente do solicitado.
     */
    ITEM_ERRADO("Item errado"),

    /**
     * Embalagem violada.
     */
    EMBALAGEM_VIOLADA("Embalagem violada"),

    /**
     * Outras divergências.
     */
    OUTROS("Outros");

    private final String descricao;

    TipoDivergenciaRecebimento(String descricao) {
        this.descricao = descricao;
    }
}
