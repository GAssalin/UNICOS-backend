package br.com.unicos.ms_auth.dto;

import java.util.Set;

/**
 * DTO para criação e atualização de papéis (roles).
 */
public record RoleRequest(
        String nome,
        String descricao,
        Set<Long> permissoesIds
) {}