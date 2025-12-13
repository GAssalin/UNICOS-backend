package br.com.unicos.ms_auth.dto.empresarolepermissao;

/**
 * DTO para listagem de permissões por role e empresa.
 */
public record EmpresaRolePermissaoListDTO(

        Long id,

        Long empresaId,

        Long roleId,
        String roleNome,

        Long permissaoId,
        String permissaoNome,

        Boolean ativo

) {}
