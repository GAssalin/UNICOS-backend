package br.com.unicos.ms_auth.dto.verificacao;

/**
 * DTO utilizado para confirmar o e-mail do usuário
 * através do token recebido por e-mail.
 */
public record ConfirmarEmailVerificacaoRequest(
        String token
) {}
