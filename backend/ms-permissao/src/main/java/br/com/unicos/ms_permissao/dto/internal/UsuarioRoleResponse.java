package br.com.unicos.ms_permissao.dto.internal;

/**
 * DTO interno com a role atribuída ao usuário.
 */
public record UsuarioRoleResponse(
        Long usuarioId,
        Long roleId
) {}
