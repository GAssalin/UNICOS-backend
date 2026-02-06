package br.com.unicos.core.produto.events.envelope;

/**
 * Factory utilitária para criação de envelopes de eventos.
 */
public final class EventEnvelopeFactory {

    private EventEnvelopeFactory() {
    }

    public static <T> EventEnvelope<T> of(EventMetadata metadata, T payload) {
        return new EventEnvelope<>(metadata, payload);
    }
}
