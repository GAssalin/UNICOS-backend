package br.com.unicos.ms_pessoas.dto.pessoa;

import br.com.unicos.ms_pessoas.enums.TipoPessoa;

/**
 * DTO de retorno que representa os dados básicos de uma pessoa.
 * <p>
 * Pode ser utilizado como base em respostas para Pessoa Física e Jurídica.
 */
public record PessoaResponse(
        Long id,
        String nome,
        TipoPessoa tipoPessoa
) {}
