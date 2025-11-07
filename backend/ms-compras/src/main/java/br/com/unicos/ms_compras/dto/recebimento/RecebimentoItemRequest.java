package br.com.unicos.ms_compras.dto.recebimento;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO utilizado para criação ou atualização de um item de recebimento de compra.
 *
 * <p>Contém os dados de produto, quantidades e observações.</p>
 */
public record RecebimentoItemRequest(

        /**
         * Identificador do produto recebido (referência ao ms-produtos).
         */
        @NotNull(message = "O identificador do produto é obrigatório.")
        Long produtoId,

        /**
         * Quantidade recebida fisicamente.
         */
        @NotNull(message = "A quantidade recebida é obrigatória.")
        @Positive(message = "A quantidade recebida deve ser maior que zero.")
        BigDecimal quantidadeRecebida,

        /**
         * Quantidade prevista no pedido original.
         */
        BigDecimal quantidadePrevista,

        /**
         * Quantidade devolvida, se aplicável.
         */
        BigDecimal quantidadeDevolvida,

        /**
         * Observações sobre o item (avarias, divergências, etc.).
         */
        String observacao,

        /**
         * Identificador do recebimento ao qual o item pertence.
         */
        @NotNull(message = "O identificador do recebimento é obrigatório.")
        Long recebimentoCompraId
) {}
