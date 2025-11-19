package br.com.unicos.ms_auth.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * DTO utilizado para criação ou atualização de usuários.
 * <p>
 * Contém apenas os dados necessários para operações de escrita,
 * não expondo informações sensíveis ou geradas automaticamente.
 */
public record UsuarioRequest(

        @NotBlank(message = "O login é obrigatório.")
        @Size(min = 4, max = 100, message = "O login deve ter entre 4 e 100 caracteres.")
        String login,

        Long pessoaId,

        @NotBlank(message = "A senha é obrigatória.")
        String password,

        @Email(message = "Informe um e-mail válido.")
        String email,

        Boolean ativo,

        /**
         * IDs das roles atribuídas a este usuário.
         */
        Set<Long> rolesIds
) {}
