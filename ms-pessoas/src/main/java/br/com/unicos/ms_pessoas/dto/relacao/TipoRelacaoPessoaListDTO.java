package br.com.unicos.ms_pessoas.dto.relacao;

/**
 * DTO utilizado em listagens de tipos de relação,
 * contendo apenas os dados essenciais.
 */
public record TipoRelacaoPessoaListDTO(
        Long id,
        String nome
) {}
