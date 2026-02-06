package br.com.unicos.core.produto.events.envelope;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Envelope padrão de eventos.
 *
 * @param metadata metadados do evento
 * @param payload conteúdo específico do evento
 * @param <T> tipo do payload
 */
public record EventEnvelope<T>(
        @JsonUnwrapped
        EventMetadata metadata,

        T payload
) { }
