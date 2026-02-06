package br.com.unicos.core.produto.events.v1.payload;

import java.math.BigDecimal;

/**
 * Payload que representa um valor monetário.
 *
 * @param valor Valor numérico.
 * @param moeda Código da moeda (ISO 4217), ex.: BRL, USD.
 */
public record DinheiroPayload(
        BigDecimal valor,
        String moeda
) { }