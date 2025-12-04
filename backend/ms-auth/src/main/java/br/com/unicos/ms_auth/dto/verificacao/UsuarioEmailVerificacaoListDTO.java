package br.com.unicos.ms_auth.dto.verificacao;

import java.time.LocalDateTime;

/**
 * DTO utilizado para listagem de tokens de verificação de e-mail.
 */
public record UsuarioEmailVerificacaoListDTO(
        Long id,
        Long usuarioId,
        boolean utilizado,
        LocalDateTime expiracao
) {}
