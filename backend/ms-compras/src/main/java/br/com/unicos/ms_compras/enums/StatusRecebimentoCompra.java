package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Status do recebimento de mercadorias.
 */
@Getter
public enum StatusRecebimentoCompra {

    /**
     * Mercadorias em processo de conferência.
     */
    EM_CONFERENCIA("Em conferência"),

    /**
     * Recebimento concluído sem divergências.
     */
    CONCLUIDO("Concluído"),

    /**
     * Recebimento concluído com divergências.
     */
    CONCLUIDO_COM_DIVERGENCIA("Concluído com divergência"),

    /**
     * Recebimento cancelado.
     */
    CANCELADO("Cancelado");

    private final String descricao;

    StatusRecebimentoCompra(String descricao) {
        this.descricao = descricao;
    }
}
