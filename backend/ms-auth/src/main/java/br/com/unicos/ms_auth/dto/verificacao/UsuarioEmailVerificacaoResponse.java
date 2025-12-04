package br.com.unicos.ms_auth.dto.verificacao;

import java.time.LocalDateTime;

/**
 * DTO utilizado para retornar informações sobre um token de verificação de e-mail.
 */
public record UsuarioEmailVerificacaoResponse(
        Long id,
        Long usuarioId,
        LocalDateTime expiracao,
        boolean utilizado,
        LocalDateTime criadoEm
) {}
