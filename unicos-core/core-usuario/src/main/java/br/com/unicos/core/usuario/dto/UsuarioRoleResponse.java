package br.com.unicos.core.usuario.dto;

/**
 * DTO com a role atribuída ao usuário.
 */
public record UsuarioRoleResponse(
        String nomeUsuario,
        String nomeRoleUsuario
) {}
