package br.com.unicos.ms_pagamento.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de forma de pagamento aceitos pela empresa.
 * <p>
 * Utilizado para identificar o meio de quitação utilizado em uma transação.
 */
@Getter
public enum TipoFormaPagamento {

    /**
     * Pagamento em dinheiro.
     */
    DINHEIRO("Dinheiro"),

    /**
     * Pagamento via cartão de crédito.
     */
    CARTAO_CREDITO("Cartão de Crédito"),

    /**
     * Pagamento via cartão de débito.
     */
    CARTAO_DEBITO("Cartão de Débito"),

    /**
     * Pagamento via PIX.
     */
    PIX("Pix"),

    /**
     * Pagamento via boleto bancário.
     */
    BOLETO("Boleto"),

    /**
     * Pagamento via transferência bancária.
     */
    TRANSFERENCIA("Transferência");

    private final String descricao;

    TipoFormaPagamento(String descricao) {
        this.descricao = descricao;
    }
}