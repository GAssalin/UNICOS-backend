package br.com.unicos.core.produto.events.v1.payload;

import java.util.UUID;

/**
 * Payload que representa uma categoria de produto.
 *
 * @param id        Identificador da categoria.
 * @param nome      Nome da categoria.
 * @param descricao Descrição da categoria.
 * @param ativa     Indica se a categoria está ativa.
 */
public record CategoriaPayload(
        UUID id,
        String nome,
        String descricao,
        boolean ativa
) { }