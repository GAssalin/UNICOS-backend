package br.com.unicos.ms_pessoas.usuario.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de usuários.
 */
public record UsuarioRequest(
        @NotBlank @Size(min = 4, max = 100) String login,
        Long pessoaId,
        // BCrypt considera no máximo 72 bytes da senha.
        @NotBlank @Size(max = 72) String password,
        @NotBlank @Email @Size(max = 150) String email,
        Boolean ativo,
        @NotNull Long roleId
) {}
