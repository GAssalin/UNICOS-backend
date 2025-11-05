package br.com.unicos.ms_estoque.dto;

/**
 * DTO de retorno com os dados completos de uma movimentação de estoque.
 */
public record MovimentacaoEstoqueResponse(
        Long id,
        Long transacaoId,
        Long produtoEstoqueId,
        Double quantidade,
        Long loteId
) {}
