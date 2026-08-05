package br.com.unicos.ms_pessoas.dto.pessoa;

/**
 * DTO utilizado em listagens de Pessoas Físicas,
 * trazendo apenas informações essenciais.
 */
public record PessoaFisicaListDTO(
        Long id,
        String nome,
        String cpf
) {}
