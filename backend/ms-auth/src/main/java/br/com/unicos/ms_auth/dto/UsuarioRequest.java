package br.com.unicos.ms_auth.dto;

import java.util.Set;

/**
 * DTO para criação e atualização de usuários.
 */
public record UsuarioRequest(
        String username,
        String password,
        String email,
        Boolean ativo,
        Set<Long> rolesIds
) { }