package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Status da cotação de compra.
 */
@Getter
public enum StatusCotacaoCompra {

    /**
     * Cotação aberta aguardando propostas.
     */
    ABERTA("Aberta"),

    /**
     * Propostas em coleta.
     */
    EM_COLETA("Em coleta de propostas"),

    /**
     * Cotação encerrada após escolha.
     */
    ENCERRADA("Encerrada"),

    /**
     * Cotação cancelada.
     */
    CANCELADA("Cancelada"),

    /**
     * Cotação expirou sem conclusão.
     */
    VENCIDA("Vencida");

    private final String descricao;

    StatusCotacaoCompra(String descricao) {
        this.descricao = descricao;
    }
}
