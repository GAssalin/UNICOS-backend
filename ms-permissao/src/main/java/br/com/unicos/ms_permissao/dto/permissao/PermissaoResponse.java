package br.com.unicos.ms_permissao.dto.permissao;

/**
 * DTO utilizado para retornar informações completas sobre
 * uma permissão cadastrada no sistema.
 */
public record PermissaoResponse(
        Long id,
        String nome,
        String descricao
) {}