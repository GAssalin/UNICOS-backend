package br.com.unicos.ms_estoque.dto.estoqueproduto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO utilizado para atualização de {@code EstoqueProduto}.
 *
 * @param estoqueId identificador do estoque.
 * @param produtoId identificador do produto.
 * @param quantidadeAtual quantidade atual do produto no estoque.
 * @param quantidadeReservada quantidade reservada do produto no estoque.
 * @param quantidadeDisponivel quantidade disponível do produto no estoque.
 */
public record EstoqueProdutoUpdateRequestDto(

        @NotNull(message = "O identificador do estoque é obrigatório.")
        Long estoqueId,

        @NotNull(message = "O identificador do produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "A quantidade atual é obrigatória.")
        @DecimalMin(value = "0.0000", inclusive = true, message = "A quantidade atual não pode ser negativa.")
        BigDecimal quantidadeAtual,

        @NotNull(message = "A quantidade reservada é obrigatória.")
        @DecimalMin(value = "0.0000", inclusive = true, message = "A quantidade reservada não pode ser negativa.")
        BigDecimal quantidadeReservada,

        @NotNull(message = "A quantidade disponível é obrigatória.")
        @DecimalMin(value = "0.0000", inclusive = true, message = "A quantidade disponível não pode ser negativa.")
        BigDecimal quantidadeDisponivel
) { }