package br.com.unicos.ms_autenticacao.dto.token;

public record DadosTokenDto(
        String tokenAccess,
        String refreshToken
) { }