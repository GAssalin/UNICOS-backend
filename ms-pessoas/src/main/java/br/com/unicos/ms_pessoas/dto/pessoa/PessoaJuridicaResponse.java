package br.com.unicos.ms_pessoas.dto.pessoa;

/**
 * DTO de retorno que representa uma Pessoa Jurídica completa.
 */
public record PessoaJuridicaResponse(
        Long id,
        String nome,
        String cnpj,
        String razaoSocial,
        String nomeFantasia
) {}
