package br.com.unicos.ms_auth.dto.role_permissao;

/**
 * DTO enxuto para validações de autorização.
 */
public record RolePermissaoResumoDTO(

        Long empresaId,
        String roleNome,
        String permissaoNome

) {}