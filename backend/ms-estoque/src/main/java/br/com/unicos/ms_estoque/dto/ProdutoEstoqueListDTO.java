package br.com.unicos.ms_estoque.dto;

/**
 * DTO resumido para listagens de produtos em estoque.
 */
public record ProdutoEstoqueListDTO(
        Long id,
        Long produtoId,
        Double quantidade
) {}
