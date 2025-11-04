package br.com.unicos.ms_auth.dto;

import java.time.LocalDateTime;

/**
 * DTO de resposta com informações de auditoria de acesso.
 */
public record AuditoriaAcessoResponse(
        Long id,
        String username,
        String acao,
        String detalhes,
        LocalDateTime dataEvento,
        String ip
) {}