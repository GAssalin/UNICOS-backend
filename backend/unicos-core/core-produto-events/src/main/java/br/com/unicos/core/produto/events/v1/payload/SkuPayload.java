package br.com.unicos.core.produto.events.v1.payload;

/**
 * Payload que representa o SKU de um produto.
 *
 * @param codigo Código único do SKU.
 */
public record SkuPayload(
        String codigo
) { }