package br.com.unicos.ms_autenticacao.dto.token;

import jakarta.validation.constraints.NotBlank;

public record DadosRefreshToken(
        @NotBlank String refreshToken
) { }
