package br.com.unicos.ms_pagamento.enums;

import lombok.Getter;

/**
 * Enum que representa o tipo de transação vinculada ao pagamento.
 * <p>
 * Define a origem ou natureza da movimentação financeira.
 */
@Getter
public enum TipoTransacao {

    /**
     * Transação originada de uma venda.
     */
    VENDA("Venda"),

    /**
     * Transação originada de uma compra.
     */
    COMPRA("Compra"),

    /**
     * Transação de natureza diversa (ex: ajuste, devolução, transferência).
     */
    OUTROS("Outros");

    private final String descricao;

    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }
}