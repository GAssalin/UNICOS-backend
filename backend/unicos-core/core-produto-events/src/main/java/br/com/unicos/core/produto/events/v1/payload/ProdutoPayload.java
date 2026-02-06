package br.com.unicos.core.produto.events.v1.payload;

import java.util.UUID;

/**
 * Payload que representa os dados principais de um produto.
 *
 * @param id           Identificador do produto.
 * @param nome         Nome do produto.
 * @param descricao    Descrição do produto.
 * @param sku          SKU do produto.
 * @param categoria    Categoria associada.
 * @param preco        Preço do produto.
 * @param ativo        Indica se o produto está ativo.
 */
public record ProdutoPayload(
        UUID id,
        String nome,
        String descricao,
        SkuPayload sku,
        CategoriaPayload categoria,
        DinheiroPayload preco,
        boolean ativo
) { }