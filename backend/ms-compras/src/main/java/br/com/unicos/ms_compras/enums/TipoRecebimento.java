package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de recebimentos possíveis no processo de compras.
 */
@Getter
public enum TipoRecebimento {

    /**
     * Recebimento total do pedido.
     */
    TOTAL("Recebimento integral do pedido de compra."),

    /**
     * Recebimento parcial do pedido.
     */
    PARCIAL("Recebimento parcial do pedido, com itens pendentes."),

    /**
     * Recebimento devolvido ao fornecedor.
     */
    DEVOLVIDO("Recebimento cancelado, com devolução de mercadorias."),

    /**
     * Recebimento pendente de conferência física.
     */
    PENDENTE_CONFERENCIA("Recebimento aguardando conferência física.");

    private final String descricao;

    TipoRecebimento(String descricao) {
        this.descricao = descricao;
    }
}
