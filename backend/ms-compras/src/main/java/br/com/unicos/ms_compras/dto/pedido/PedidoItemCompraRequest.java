package br.com.unicos.ms_compras.dto.pedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO utilizado para criação ou atualização de itens do pedido de compra.
 *
 * <p>
 * Contém apenas os campos necessários para entrada de dados via API.
 * </p>
 */
public record PedidoItemCompraRequest(

        @NotNull(message = "O ID do produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        BigDecimal quantidade,

        @NotNull(message = "O preço unitário é obrigatório.")
        @Positive(message = "O preço unitário deve ser maior que zero.")
        BigDecimal precoUnitario,

        BigDecimal desconto
) { }
