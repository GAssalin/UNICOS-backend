package br.com.erp.ms_pagamento.enums;

/**
 * Enum que representa os tipos de conta financeira existentes.
 *
 * Utilizado para classificar contas bancárias ou caixas físicos.
 */
public enum TipoConta {

    /** Conta corrente bancária. */
    CONTA_CORRENTE("Conta Corrente"),

    /** Conta poupança bancária. */
    CONTA_POUPANCA("Conta Poupança"),

    /** Caixa físico da empresa. */
    CAIXA("Caixa");

    private final String descricao;

    TipoConta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}