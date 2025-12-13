package br.com.unicos.ms_auth.dto.empresarolepermissao;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para criação ou atualização de permissões de role por empresa.
 */
public record EmpresaRolePermissaoRequest(

        @NotNull(message = "Empresa é obrigatória")
        Long empresaId,

        @NotNull(message = "Role é obrigatória")
        Long roleId,

        @NotNull(message = "Permissão é obrigatória")
        Long permissaoId,

        Boolean ativo

) {}
