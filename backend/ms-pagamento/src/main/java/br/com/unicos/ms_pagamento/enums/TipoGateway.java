package br.com.unicos.ms_pagamento.enums;

import lombok.Getter;

/**
 * Enum que representa os diferentes provedores (gateways) de pagamento
 * integrados ao sistema UniCoS.
 *
 * <p>
 * Cada tipo de gateway define um conjunto distinto de regras de autenticação
 * e comunicação com APIs externas.
 */
@Getter
public enum TipoGateway {

    /**
     * Integração com o provedor PagSeguro.
     */
    PAGSEGURO("PagSeguro"),

    /**
     * Integração com o provedor Mercado Pago.
     */
    MERCADO_PAGO("Mercado Pago"),

    /**
     * Integração com o provedor Stripe.
     */
    STRIPE("Stripe"),

    /**
     * Integração com o provedor Pagar.me.
     */
    PAGARME("Pagar.me"),

    /**
     * Integração com o provedor Inter.
     */
    BANCO_INTER("Banco Inter"),

    /**
     * Integração genérica com outro gateway personalizado.
     */
    PERSONALIZADO("Gateway Personalizado");

    private final String descricao;

    TipoGateway(String descricao) {
        this.descricao = descricao;
    }
}
