package br.com.unicos.ms_pessoas.dto.contato;

import br.com.unicos.ms_pessoas.enums.TipoContato;

/**
 * DTO de retorno que representa um contato completo associado a uma pessoa.
 */
public record ContatoResponse(
        Long id,
        Long pessoaId,
        TipoContato tipo,
        String valor,
        Boolean principal
) {}
