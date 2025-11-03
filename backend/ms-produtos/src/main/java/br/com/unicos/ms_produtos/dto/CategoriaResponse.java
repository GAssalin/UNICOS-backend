package br.com.unicos.ms_produtos.dto;

/**
 * DTO usado para retorno detalhado de categoria.
 */
public record CategoriaResponse(
        Long id,
        String nome,
        String descricao
) {}