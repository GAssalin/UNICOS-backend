package br.com.unicos.ms_pessoas.dto.pessoa;

/**
 * DTO utilizado em listagens de Pessoas Jurídicas,
 * trazendo apenas informações essenciais.
 */
public record PessoaJuridicaListDTO(
        Long id,
        String nome,
        String cnpj
) {}
