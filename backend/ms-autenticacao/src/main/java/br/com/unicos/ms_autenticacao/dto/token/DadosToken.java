package br.com.unicos.ms_autenticacao.dto.token;

public record DadosToken(
        String tokenAccess,
        String refreshToken
) { }