package br.com.unicos.ms_estoque.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação de movimentações de lotes.
 */
public record MovimentacaoLoteRequest(
        @NotNull Long movimentacaoId,
        @NotNull Long loteId,
        @NotNull Double quantidade
) {}
