package br.com.unicos.ms_auth.dto.permissao;

/**
 * DTO utilizado para listagens de permissões,
 * apresentando apenas os dados essenciais.
 */
public record PermissaoListDTO(
        Long id,
        String codigo
) {}