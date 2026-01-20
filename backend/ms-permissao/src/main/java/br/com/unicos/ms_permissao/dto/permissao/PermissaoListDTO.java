package br.com.unicos.ms_permissao.dto.permissao;

/**
 * DTO utilizado para listagens de permissões,
 * apresentando apenas os dados essenciais.
 */
public record PermissaoListDTO(
        Long id,
        String codigo
) {}