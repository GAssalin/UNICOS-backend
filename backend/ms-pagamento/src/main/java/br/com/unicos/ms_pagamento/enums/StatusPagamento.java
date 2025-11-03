package br.com.unicos.ms_pagamento.enums;

import lombok.Getter;

/**
 * Enum que representa o status de um pagamento.
 * <p>
 * Indica a situação atual do pagamento em relação ao seu ciclo financeiro.
 */
@Getter
public enum StatusPagamento {

    /**
     * Pagamento pendente de quitação.
     */
    PENDENTE("Pendente"),

    /**
     * Pagamento quitado integralmente.
     */
    PAGO("Pago"),

    /**
     * Pagamento cancelado antes da conclusão.
     */
    CANCELADO("Cancelado"),

    /**
     * Pagamento vencido e ainda não quitado.
     */
    ATRASADO("Atrasado"),

    /**
     * Pagamento parcialmente quitado.
     */
    PARCIAL("Parcial");

    private final String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }
}