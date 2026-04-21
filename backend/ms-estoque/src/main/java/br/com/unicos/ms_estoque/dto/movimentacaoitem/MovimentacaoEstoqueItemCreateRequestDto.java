package br.com.unicos.ms_estoque.dto.movimentacaoitem;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO utilizado para criação de {@code MovimentacaoEstoqueItem}.
 *
 * @param movimentacaoId identificador da movimentação.
 * @param produtoId identificador do produto.
 * @param quantidade quantidade movimentada.
 * @param valorUnitario valor unitário do item.
 */
public record MovimentacaoEstoqueItemCreateRequestDto(

        @NotNull(message = "O identificador da movimentação é obrigatório.")
        Long movimentacaoId,

        @NotNull(message = "O identificador do produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "A quantidade é obrigatória.")
        @DecimalMin(value = "0.0001", inclusive = true, message = "A quantidade deve ser maior que zero.")
        BigDecimal quantidade,

        @DecimalMin(value = "0.0000", inclusive = true, message = "O valor unitário não pode ser negativo.")
        BigDecimal valorUnitario
) { }