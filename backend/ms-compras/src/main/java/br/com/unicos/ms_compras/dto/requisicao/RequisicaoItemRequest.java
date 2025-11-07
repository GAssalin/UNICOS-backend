package br.com.unicos.ms_compras.dto.requisicao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO utilizado para criação ou atualização de um item de requisição de compra.
 *
 * <p>Contém os dados de produto, quantidades e observações adicionais do solicitante.</p>
 */
public record RequisicaoItemRequest(

        /**
         * Identificador do produto solicitado (referência ao ms-produtos).
         */
        @NotNull(message = "O identificador do produto é obrigatório.")
        Long produtoId,

        /**
         * Quantidade solicitada pelo requisitante.
         */
        @NotNull(message = "A quantidade solicitada é obrigatória.")
        @Positive(message = "A quantidade solicitada deve ser maior que zero.")
        BigDecimal quantidadeSolicitada,

        /**
         * Quantidade atendida pela compra (pode ser preenchida posteriormente).
         */
        BigDecimal quantidadeAtendida,

        /**
         * Observação sobre o item (justificativa, prioridade, etc.).
         */
        String observacao,

        /**
         * Identificador da requisição à qual o item pertence.
         */
        @NotNull(message = "O identificador da requisição é obrigatório.")
        Long requisicaoCompraId
) {}
