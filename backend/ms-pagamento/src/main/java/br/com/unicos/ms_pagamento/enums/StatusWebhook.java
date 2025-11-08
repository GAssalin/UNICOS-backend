package br.com.unicos.ms_pagamento.enums;

import lombok.Getter;

/**
 * Enum que representa o status de processamento de um webhook
 * recebido de um gateway de pagamento.
 *
 * <p>
 * Permite acompanhar o ciclo de vida do callback desde o momento do
 * recebimento até sua efetiva conciliação no sistema.
 */
@Getter
public enum StatusWebhook {

    /**
     * Webhook recebido, mas ainda não processado.
     */
    PENDENTE("Pendente"),

    /**
     * Webhook processado com sucesso.
     */
    PROCESSADO("Processado"),

    /**
     * Ocorreu um erro durante o processamento do webhook.
     */
    ERRO("Erro no processamento"),

    /**
     * Webhook foi ignorado por ser duplicado ou irrelevante.
     */
    IGNORADO("Ignorado");

    private final String descricao;

    StatusWebhook(String descricao) {
        this.descricao = descricao;
    }
}
