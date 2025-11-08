package br.com.unicos.ms_pagamento.dto.gateway;

import br.com.unicos.ms_pagamento.enums.StatusWebhook;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO de entrada para registrar um webhook recebido
 * de um provedor de pagamento externo.
 *
 * <p>
 * Contém os dados essenciais enviados pelo gateway
 * e usados para conciliação e auditoria no UniCoS.
 */
public record WebhookPagamentoRequest(

        /** Identificador da transação no sistema externo (gateway). */
        @Size(max = 100, message = "O código externo deve ter no máximo 100 caracteres.")
        String codigoExterno,

        /** Data e hora em que o webhook foi recebido. */
        @NotNull(message = "A data de recebimento é obrigatória.")
        LocalDateTime dataRecebimento,

        /** Corpo da mensagem original enviada pelo provedor (JSON). */
        @NotBlank(message = "O payload do webhook é obrigatório.")
        String payload,

        /** Status de processamento interno do webhook. */
        @NotNull(message = "O status de processamento é obrigatório.")
        StatusWebhook statusProcessamento,

        /** Mensagem de erro ou log de processamento (caso aplicável). */
        @Size(max = 255, message = "A mensagem de processamento deve ter no máximo 255 caracteres.")
        String mensagemProcessamento,

        /** ID da transação de pagamento associada ao webhook. */
        @NotNull(message = "O ID da transação associada é obrigatório.")
        Long transacaoPagamentoId
) { }
