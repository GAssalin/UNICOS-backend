package br.com.unicos.ms_pessoas.dto;

/**
 * DTO de retorno para os tipos de relação de pessoa.
 */
public record TipoRelacaoPessoaResponse(
        Long id,
        String codigo,
        String descricao,
        boolean ativo
) {}