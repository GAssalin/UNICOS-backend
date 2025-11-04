package br.com.unicos.ms_pessoas.dto;

import br.com.unicos.ms_pessoas.enums.TipoContato;

/**
 * DTO de retorno de contatos de pessoa.
 */
public record ContatoPessoaResponse(
        Long id,
        Long pessoaId,
        TipoContato tipoContato,
        String valor,
        String descricao,
        boolean principal
) {}