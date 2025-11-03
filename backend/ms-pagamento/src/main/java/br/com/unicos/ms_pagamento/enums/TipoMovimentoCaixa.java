package br.com.unicos.ms_pagamento.enums;

/**
 * Enum que representa o tipo de movimento realizado no caixa ou conta financeira.
 *
 * Determina se o valor movimentado representa uma entrada ou saída de recursos.
 */
public enum TipoMovimentoCaixa {

    /** Entrada de valores (recebimento, depósito, etc). */
    ENTRADA("Entrada"),

    /** Saída de valores (pagamento, saque, transferência). */
    SAIDA("Saída");

    private final String descricao;

    TipoMovimentoCaixa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}