package br.com.unicos.ms_autenticacao.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Credenciais de login. Os limites acompanham o cadastro de usuários no ms-pessoas
 * (e-mail com até 150 caracteres e senha com até 72, o máximo considerado pelo BCrypt).
 */
public record DadosLoginDto(
        @NotBlank @Size(max = 150) String email,
        @NotBlank @Size(max = 72) String senha
) {}
