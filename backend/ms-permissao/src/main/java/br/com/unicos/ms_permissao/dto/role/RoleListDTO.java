package br.com.unicos.ms_permissao.dto.role;

/**
 * DTO utilizado para listagens de papéis,
 * apresentando apenas os dados essenciais.
 */
public record RoleListDTO(
        Long id,
        String codigo,
        String descricao
) {}