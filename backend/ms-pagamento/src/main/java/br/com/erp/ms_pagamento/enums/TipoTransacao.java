package br.com.erp.ms_pagamento.enums;

/**
 * Enum que representa o tipo de transação vinculada ao pagamento.
 *
 * Define a origem ou natureza da movimentação financeira.
 */
public enum TipoTransacao {

    /** Transação originada de uma venda. */
    VENDA("Venda"),

    /** Transação originada de uma compra. */
    COMPRA("Compra"),

    /** Transação de natureza diversa (ex: ajuste, devolução, transferência). */
    OUTROS("Outros");

    private final String descricao;

    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}