package br.com.unicos.ms_auth.dto.empresarolepermissao;

import java.time.LocalDateTime;

/**
 * DTO de retorno da associação empresa x role x permissão.
 */
public record EmpresaRolePermissaoResponse(

        Long id,

        Long empresaId,

        Long roleId,
        String roleNome,

        Long permissaoId,
        String permissaoNome,

        Boolean ativo,

        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm

) {}
