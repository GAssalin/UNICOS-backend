package br.com.unicos.ms_auth.dto.token;

public record DadosToken(
        String tokenAcesso,
        String refreshToken
) { }