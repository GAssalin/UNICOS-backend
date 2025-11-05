package br.com.unicos.ms_ativos.dto;

import java.time.LocalDateTime;

/**
 * DTO de resposta utilizado para exibir informações detalhadas
 * de um evento do histórico de ativo.
 */
public record HistoricoAtivoResponse(

        /** Identificador único do registro de histórico. */
        Long id,

        /** Identificador do ativo relacionado. */
        Long ativoId,

        /** Nome do ativo (para exibição em relatórios). */
        String nomeAtivo,

        /** Data e hora do evento. */
        LocalDateTime dataEvento,

        /** Descrição do evento ocorrido. */
        String descricaoEvento,

        /** Identificador do usuário responsável pelo evento. */
        Long usuarioResponsavelId
) { }
