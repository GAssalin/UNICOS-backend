package br.com.unicos.core.usuario.auth.dto;

/**
 * DTO com a role atribuída ao usuário.
 */
public record UsuarioRoleResponse(
        String nomeUsuario,
        String nomeRoleUsuario
) {}
