package br.com.unicos.ms_pagamento.enums;

/**
 * Enum que representa o status de uma transação financeira.
 *
 * Utilizado para identificar o estado de processamento junto a gateways
 * de pagamento ou sistemas bancários.
 */
public enum StatusTransacao {

    /** Transação em processamento ou aguardando confirmação. */
    PROCESSANDO("Processando"),

    /** Transação concluída com sucesso. */
    CONFIRMADA("Confirmada"),

    /** Transação que apresentou falha ou erro. */
    FALHA("Falha");

    private final String descricao;

    StatusTransacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}