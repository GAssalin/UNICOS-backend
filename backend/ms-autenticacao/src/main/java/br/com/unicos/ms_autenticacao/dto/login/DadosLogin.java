package br.com.unicos.ms_autenticacao.dto.login;

import jakarta.validation.constraints.NotBlank;

public record DadosLogin(
        @NotBlank String email,
        @NotBlank String senha
) {}
