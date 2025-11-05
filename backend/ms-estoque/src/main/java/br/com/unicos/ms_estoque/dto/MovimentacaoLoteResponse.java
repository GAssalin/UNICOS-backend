package br.com.unicos.ms_estoque.dto;

/**
 * DTO de retorno com os dados de movimentações de lotes.
 */
public record MovimentacaoLoteResponse(
        Long id,
        Long movimentacaoId,
        Long loteId,
        Double quantidade
) {}
