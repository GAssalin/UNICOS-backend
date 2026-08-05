package br.com.unicos.ms_estoque.dto.movimentacaoitem;

import java.math.BigDecimal;

/**
 * DTO utilizado para retorno de dados de {@code MovimentacaoEstoqueItem}.
 *
 * @param id identificador do item.
 * @param movimentacaoId identificador da movimentação.
 * @param produtoId identificador do produto.
 * @param quantidade quantidade movimentada.
 * @param valorUnitario valor unitário do item.
 */
public record MovimentacaoEstoqueItemResponseDto(
        Long id,
        Long movimentacaoId,
        Long produtoId,
        BigDecimal quantidade,
        BigDecimal valorUnitario
) { }