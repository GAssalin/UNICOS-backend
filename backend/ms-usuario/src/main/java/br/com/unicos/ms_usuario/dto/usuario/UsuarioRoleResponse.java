package br.com.unicos.ms_usuario.dto.usuario;

/**
 * DTO interno com a role atualmente atribuída ao usuário.
 */
public record UsuarioRoleResponse(
        Long usuarioId,
        Long roleId
) {}
