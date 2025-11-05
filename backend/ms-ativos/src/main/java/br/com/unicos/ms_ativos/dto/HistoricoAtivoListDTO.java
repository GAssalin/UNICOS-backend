package br.com.unicos.ms_ativos.dto;

import java.time.LocalDateTime;

/**
 * DTO simplificado para listagem de eventos do histórico de ativos.
 * <p>
 * Usado em consultas de auditoria, dashboards e relatórios resumidos.
 */
public record HistoricoAtivoListDTO(

        /** Identificador único do registro de histórico. */
        Long id,

        /** Data e hora do evento. */
        LocalDateTime dataEvento,

        /** Descrição resumida do evento. */
        String descricaoEvento,

        /** Identificador do usuário responsável. */
        Long usuarioResponsavelId
) { }
