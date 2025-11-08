package br.com.unicos.ms_pagamento.dto.gateway;

import br.com.unicos.ms_pagamento.enums.StatusWebhook;

import java.time.LocalDateTime;

/**
 * DTO de saída que representa um webhook registrado
 * e processado pelo sistema UniCoS.
 *
 * <p>
 * Retorna informações de auditoria, status de processamento
 * e vínculo com a transação de pagamento.
 */
public record WebhookPagamentoResponse(

        /** Identificador único do webhook no sistema. */
        Long id,

        /** Código externo do webhook (identificação do gateway). */
        String codigoExterno,

        /** Data e hora de recebimento da notificação. */
        LocalDateTime dataRecebimento,

        /** Conteúdo bruto recebido do provedor (JSON original). */
        String payload,

        /** Status de processamento interno (pendente, processado, erro, ignorado). */
        StatusWebhook statusProcessamento,

        /** Mensagem de log ou erro durante o processamento. */
        String mensagemProcessamento,

        /** Identificador da transação de pagamento associada. */
        Long transacaoPagamentoId,

        /** Data de criação do registro (auditoria). */
        LocalDateTime dataCriacao,

        /** Data da última atualização (auditoria). */
        LocalDateTime dataAtualizacao
) {}
