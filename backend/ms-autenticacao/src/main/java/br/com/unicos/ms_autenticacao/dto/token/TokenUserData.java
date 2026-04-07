package br.com.unicos.ms_autenticacao.dto.token;

public record TokenUserData(
        Long userId,
        String username,
        Long tenantId
) { }