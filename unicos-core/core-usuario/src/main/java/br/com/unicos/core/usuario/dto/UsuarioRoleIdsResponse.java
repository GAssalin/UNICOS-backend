package br.com.unicos.core.usuario.dto;

/**
 * DTO com idUsuario e idRole.
 */
public record UsuarioRoleIdsResponse(
        Long idUsuario,
        Long idRole
) {}
