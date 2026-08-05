package br.com.unicos.ms_estoque.dto.estoqueproduto;

/**
 * DTO utilizado para filtros de pesquisa de {@code EstoqueProduto}.
 *
 * @param estoqueId filtra por estoque.
 * @param produtoId filtra por produto.
 */
public record EstoqueProdutoSearchRequestDto(
        Long estoqueId,
        Long produtoId
) { }