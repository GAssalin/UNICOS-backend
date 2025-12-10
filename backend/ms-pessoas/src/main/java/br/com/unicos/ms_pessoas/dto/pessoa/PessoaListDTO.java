package br.com.unicos.ms_pessoas.dto.pessoa;

import br.com.unicos.ms_pessoas.enums.TipoPessoa;

/**
 * DTO utilizado para listagem de pessoas,
 * apresentando informações essenciais para consultas rápidas.
 */
public record PessoaListDTO(
        Long id,
        String nome,
        TipoPessoa tipoPessoa
) {}
