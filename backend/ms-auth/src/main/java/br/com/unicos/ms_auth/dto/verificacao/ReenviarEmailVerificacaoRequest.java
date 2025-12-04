package br.com.unicos.ms_auth.dto.verificacao;

/**
 * DTO utilizado para solicitar o reenvio do token de verificação de e-mail.
 */
public record ReenviarEmailVerificacaoRequest(
        Long usuarioId
) {}
