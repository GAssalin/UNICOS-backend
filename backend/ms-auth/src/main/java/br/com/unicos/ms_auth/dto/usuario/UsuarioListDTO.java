package br.com.unicos.ms_auth.dto.usuario;

import java.util.Set;

/**
 * DTO utilizado para listagens de usuários,
 * apresentando apenas dados essenciais para consultas.
 */
public record UsuarioListDTO(
        Long id,
        String login,
        String email,
        boolean ativo,
        Set<String> roles
) {}
