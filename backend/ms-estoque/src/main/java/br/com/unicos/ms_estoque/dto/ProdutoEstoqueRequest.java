package br.com.unicos.ms_estoque.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação e atualização de produtos em estoque.
 */
public record ProdutoEstoqueRequest(
        @NotNull Long produtoId,
        @NotNull Long estoqueLocalId,
        @NotNull Double quantidade,
        Double quantidadeMinima,
        Double quantidadeMaxima
) { }
