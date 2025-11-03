package br.com.unicos.ms_pagamento.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de conta financeira existentes.
 * <p>
 * Utilizado para classificar contas bancárias ou caixas físicos.
 */
@Getter
public enum TipoConta {

    /**
     * Conta corrente bancária.
     */
    CONTA_CORRENTE("Conta Corrente"),

    /**
     * Conta poupança bancária.
     */
    CONTA_POUPANCA("Conta Poupança"),

    /**
     * Caixa físico da empresa.
     */
    CAIXA("Caixa");

    private final String descricao;

    TipoConta(String descricao) {
        this.descricao = descricao;
    }
}