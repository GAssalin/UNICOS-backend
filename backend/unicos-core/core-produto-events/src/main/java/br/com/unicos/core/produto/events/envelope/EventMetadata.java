package br.com.unicos.core.produto.events.envelope;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Metadados comuns a todos os eventos publicados.
 *
 * @param eventId identificador único do evento
 * @param eventType tipo do evento
 * @param occurredAt data/hora em que o evento ocorreu
 * @param tenantId identificador do tenant (pode ser null se não aplicável)
 * @param correlationId id para rastreio distribuído
 * @param causationId id do evento que causou este (opcional)
 * @param producer serviço ou aplicação produtora
 * @param schemaVersion versão do schema do evento
 */
public record EventMetadata(
        UUID eventId,
        EventType eventType,
        OffsetDateTime occurredAt,
        String tenantId,
        UUID correlationId,
        UUID causationId,
        String producer,
        int schemaVersion
) {

    public static EventMetadata create(
            EventType eventType,
            String tenantId,
            UUID correlationId,
            String producer,
            int schemaVersion
    ) {
        return new EventMetadata(
                UUID.randomUUID(),
                eventType,
                OffsetDateTime.now(),
                tenantId,
                correlationId,
                null,
                producer,
                schemaVersion
        );
    }
}
