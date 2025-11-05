package br.com.unicos.ms_estoque.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação e atualização de movimentações de estoque.
 */
public record MovimentacaoEstoqueRequest(
        @NotNull Long transacaoId,
        @NotNull Long produtoEstoqueId,
        @NotNull Double quantidade,
        Long loteId
) {}
