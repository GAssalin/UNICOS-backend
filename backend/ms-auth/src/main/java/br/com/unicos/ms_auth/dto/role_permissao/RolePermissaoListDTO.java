package br.com.unicos.ms_auth.dto.role_permissao;

/**
 * DTO para listagem de permissões por role e empresa.
 */
public record RolePermissaoListDTO(

        Long id,

        Long empresaId,

        Long roleId,
        String roleNome,

        Long permissaoId,
        String permissaoNome,

        Boolean ativo

) {}
