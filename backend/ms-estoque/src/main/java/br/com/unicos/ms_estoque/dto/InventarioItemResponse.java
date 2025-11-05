package br.com.unicos.ms_estoque.dto;

/**
 * DTO de retorno com os dados de um item de inventário.
 */
public record InventarioItemResponse(
        Long id,
        Long inventarioId,
        Long produtoEstoqueId,
        Double quantidadeContada,
        Double quantidadeRegistrada,
        String observacao
) {}
