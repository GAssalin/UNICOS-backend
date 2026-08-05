package br.com.unicos.core.pessoas.dto.pessoa;

/**
 * DTO de retorno que representa os dados básicos de uma pessoa.
 * <p>
 * Pode ser utilizado como base em respostas para Pessoa Física e Jurídica.
 */
public record PessoaResponseClient(
        Long id,
        String nome
) {}
