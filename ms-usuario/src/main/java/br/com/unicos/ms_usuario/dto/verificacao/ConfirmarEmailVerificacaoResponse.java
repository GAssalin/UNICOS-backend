package br.com.unicos.ms_usuario.dto.verificacao;

/**
 * DTO utilizado para informar o resultado da confirmação de e-mail.
 */
public record ConfirmarEmailVerificacaoResponse(
        Long usuarioId,
        String email,
        boolean emailVerificado,
        String mensagem
) {}