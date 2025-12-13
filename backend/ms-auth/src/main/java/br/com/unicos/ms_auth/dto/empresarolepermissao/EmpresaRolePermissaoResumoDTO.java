package br.com.unicos.ms_auth.dto.empresarolepermissao;

/**
 * DTO enxuto para validações de autorização.
 */
public record EmpresaRolePermissaoResumoDTO(

        Long empresaId,
        String roleNome,
        String permissaoNome

) {}
