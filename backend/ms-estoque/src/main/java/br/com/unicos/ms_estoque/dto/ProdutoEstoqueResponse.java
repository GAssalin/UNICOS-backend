package br.com.unicos.ms_estoque.dto;

/**
 * DTO de retorno com os dados completos de um produto no estoque.
 */
public record ProdutoEstoqueResponse(
        Long id,
        Long produtoId,
        Long estoqueLocalId,
        Double quantidade,
        Double quantidadeMinima,
        Double quantidadeMaxima
) {}
