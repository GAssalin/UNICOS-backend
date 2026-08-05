package br.com.unicos.ms_estoque.dto.movimentacaoitem;

/**
 * DTO utilizado para filtros de pesquisa de {@code MovimentacaoEstoqueItem}.
 *
 * @param movimentacaoId filtra por movimentação.
 * @param produtoId filtra por produto.
 */
public record MovimentacaoEstoqueItemSearchRequestDto(
        Long movimentacaoId,
        Long produtoId
) { }