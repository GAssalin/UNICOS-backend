package br.com.unicos.ms_compras.dto.fiscal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO utilizado para criação ou atualização de um item de Nota Fiscal de Compra.
 *
 * <p>Contém as informações básicas de produto, valores e impostos.</p>
 */
public record NotaFiscalItemRequest(

        /**
         * Identificador do produto (referência ao ms-produtos).
         */
        @NotNull(message = "O identificador do produto é obrigatório.")
        Long produtoId,

        /**
         * Descrição do produto no momento da nota (texto fiscal).
         */
        @NotBlank(message = "A descrição do produto é obrigatória.")
        String descricaoProduto,

        /**
         * Quantidade de produto faturada.
         */
        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        BigDecimal quantidade,

        /**
         * Valor unitário do produto.
         */
        @NotNull(message = "O valor unitário é obrigatório.")
        @Positive(message = "O valor unitário deve ser maior que zero.")
        BigDecimal valorUnitario,

        /**
         * Valor total do item (quantidade × valor unitário).
         */
        BigDecimal valorTotal,

        /**
         * Valor de ICMS destacado.
         */
        BigDecimal valorICMS,

        /**
         * Valor de IPI destacado.
         */
        BigDecimal valorIPI,

        /**
         * Valor de desconto aplicado ao item.
         */
        BigDecimal valorDesconto,

        /**
         * Identificador da nota fiscal à qual o item pertence.
         */
        @NotNull(message = "O identificador da nota fiscal é obrigatório.")
        Long notaFiscalCompraId
) {}
