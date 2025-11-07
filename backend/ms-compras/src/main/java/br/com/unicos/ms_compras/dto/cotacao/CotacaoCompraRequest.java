package br.com.unicos.ms_compras.dto.cotacao;

import br.com.unicos.ms_compras.enums.StatusCotacao;
import br.com.unicos.ms_compras.enums.TipoCotacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO utilizado para criação ou atualização de uma cotação de compra.
 */
public record CotacaoCompraRequest(

        /**
         * Código ou número identificador da cotação.
         */
        @NotBlank(message = "O código da cotação é obrigatório.")
        String codigo,

        /**
         * Tipo da cotação (manual, automática, emergencial).
         */
        @NotNull(message = "O tipo da cotação é obrigatório.")
        TipoCotacao tipoCotacao,

        /**
         * Status atual da cotação.
         */
        @NotNull(message = "O status da cotação é obrigatório.")
        StatusCotacao status,

        /**
         * Data de abertura da cotação.
         */
        @NotNull(message = "A data de abertura é obrigatória.")
        LocalDate dataAbertura,

        /**
         * Data limite de validade da cotação.
         */
        LocalDate dataValidade,

        /**
         * Observações gerais da cotação.
         */
        String observacao
) { }
