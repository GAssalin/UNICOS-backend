package br.com.unicos.ms_autenticacao.dto.token;

public record TokenUserDataDto(
        Long userId,
        String username,
        Long tenantId
) { }