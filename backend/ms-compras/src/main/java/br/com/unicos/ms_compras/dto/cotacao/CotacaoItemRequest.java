package br.com.unicos.ms_compras.dto.cotacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO utilizado para criação ou atualização de um item de cotação.
 *
 * <p>Representa as informações básicas necessárias para registrar
 * um item vinculado a uma proposta de fornecedor.</p>
 */
public record CotacaoItemRequest(

        /**
         * Identificador do produto cotado (referência ao ms-produtos).
         */
        @NotNull(message = "O identificador do produto é obrigatório.")
        Long produtoId,

        /**
         * Quantidade solicitada para o produto.
         */
        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        BigDecimal quantidade,

        /**
         * Valor unitário ofertado pelo fornecedor.
         */
        @NotNull(message = "O valor unitário é obrigatório.")
        @Positive(message = "O valor unitário deve ser maior que zero.")
        BigDecimal valorUnitario,

        /**
         * Valor total calculado para o item (quantidade × valor unitário).
         */
        BigDecimal valorTotal,

        /**
         * Identificador da proposta do fornecedor à qual este item pertence.
         */
        @NotNull(message = "O identificador da proposta do fornecedor é obrigatório.")
        Long cotacaoFornecedorId
) { }
