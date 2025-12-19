package br.com.unicos.ms_auth.dto.role;

/**
 * DTO utilizado para retornar as informações completas de um papel,
 * incluindo suas permissões associadas.
 */
public record RoleResponse(
        Long id,
        String nome,
        String descricao
) { }