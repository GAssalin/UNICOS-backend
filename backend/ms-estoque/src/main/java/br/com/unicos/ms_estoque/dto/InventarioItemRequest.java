package br.com.unicos.ms_estoque.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para inserção e atualização de itens no inventário.
 */
public record InventarioItemRequest(
        @NotNull Long inventarioId,
        @NotNull Long produtoEstoqueId,
        @NotNull Double quantidadeContada,
        @NotNull Double quantidadeRegistrada,
        String observacao
) {}
