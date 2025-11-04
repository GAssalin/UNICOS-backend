package br.com.unicos.ms_auth.dto;

import java.util.Set;

/**
 * DTO de resposta com informações de um usuário autenticável.
 */
public record UsuarioResponse(
        Long id,
        String username,
        String email,
        Boolean ativo,
        Set<RoleResponse> roles
) {}