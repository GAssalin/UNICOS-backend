package br.com.unicos.ms_auth.dto;

import java.time.LocalDateTime;

/**
 * DTO de resposta para tokens de acesso JWT.
 */
public record TokenAcessoResponse(
        Long id,
        String token,
        LocalDateTime dataEmissao,
        LocalDateTime dataExpiracao,
        Boolean valido,
        Long usuarioId
) {}