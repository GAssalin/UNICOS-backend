package br.com.unicos.ms_compras.dto.requisicao;

import br.com.unicos.ms_compras.enums.TipoRequisicaoCompra;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO utilizado para criação ou atualização de uma requisição de compra.
 */
public record RequisicaoCompraRequest(

        /**
         * Código identificador da requisição.
         */
        @NotBlank(message = "O código da requisição é obrigatório.")
        @Size(max = 50, message = "O código deve conter no máximo 50 caracteres.")
        String codigo,

        /**
         * Tipo de requisição (reposição, interna, urgência, etc.).
         */
        @NotNull(message = "O tipo da requisição é obrigatório.")
        TipoRequisicaoCompra tipoRequisicao,

        /**
         * Data em que a requisição foi aberta.
         */
        @NotNull(message = "A data de abertura é obrigatória.")
        LocalDate dataAbertura,

        /**
         * Data limite desejada para atendimento.
         */
        LocalDate dataLimite,

        /**
         * Identificador do solicitante (FK futura para ms-pessoas).
         */
        @NotNull(message = "O identificador do solicitante é obrigatório.")
        Long solicitanteId,

        /**
         * Observações gerais sobre a requisição.
         */
        @Size(max = 500, message = "A observação deve conter no máximo 500 caracteres.")
        String observacao,

        /**
         * Itens solicitados na requisição.
         */
        List<RequisicaoItemRequest> itens
) {}
