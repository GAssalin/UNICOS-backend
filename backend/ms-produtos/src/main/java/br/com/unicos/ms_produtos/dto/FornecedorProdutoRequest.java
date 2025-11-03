package br.com.unicos.ms_produtos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 * DTO usado para criação e atualização de FornecedorProduto.
 */
public record FornecedorProdutoRequest(

        @NotNull(message = "O ID do fornecedor é obrigatório.")
        Long fornecedorId,

        @NotNull(message = "O ID do produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "O preço de custo é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço de custo deve ser maior que zero.")
        BigDecimal precoCusto,

        @PositiveOrZero(message = "O prazo de entrega deve ser zero ou positivo.")
        Integer prazoEntregaDias
) {}