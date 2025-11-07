package br.com.unicos.ms_compras.dto.cotacao;

import br.com.unicos.ms_compras.enums.StatusFornecedorCotacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO utilizado para criação ou atualização de uma proposta de fornecedor em uma cotação.
 */
public record CotacaoFornecedorRequest(

        /**
         * Identificador do fornecedor (FK futura para ms-pessoas).
         */
        @NotNull(message = "O identificador do fornecedor é obrigatório.")
        Long fornecedorId,

        /**
         * Valor total proposto pelo fornecedor.
         */
        @NotNull(message = "O valor total é obrigatório.")
        @Positive(message = "O valor total deve ser maior que zero.")
        BigDecimal valorTotal,

        /**
         * Prazo de entrega informado pelo fornecedor (em dias).
         */
        @Positive(message = "O prazo de entrega deve ser maior que zero.")
        Integer prazoEntrega,

        /**
         * Status da proposta do fornecedor.
         */
        @NotNull(message = "O status da proposta é obrigatório.")
        StatusFornecedorCotacao status,

        /**
         * Observações específicas desta proposta.
         */
        String observacao,

        /**
         * Lista de itens cotados pelo fornecedor.
         */
        List<CotacaoItemRequest> itens,

        /**
         * ID da cotação principal à qual o fornecedor está vinculado.
         */
        @NotNull(message = "O identificador da cotação é obrigatório.")
        Long cotacaoCompraId
) {}
