package br.com.unicos.ms_auth.dto;

import java.util.Set;

/**
 * DTO de resposta com informações de um papel e suas permissões.
 */
public record RoleResponse(
        Long id,
        String nome,
        String descricao,
        Set<PermissaoResponse> permissoes
) {}