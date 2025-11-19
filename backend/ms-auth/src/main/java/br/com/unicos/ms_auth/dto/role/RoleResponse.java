package br.com.unicos.ms_auth.dto.role;

import br.com.unicos.ms_auth.dto.permissao.PermissaoResponse;

import java.util.Set;

/**
 * DTO utilizado para retornar as informações completas de um papel,
 * incluindo suas permissões associadas.
 */
public record RoleResponse(
        Long id,
        String codigo,
        String descricao,
        Set<PermissaoResponse> permissoes
) {}
