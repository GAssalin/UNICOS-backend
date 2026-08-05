package br.com.unicos.ms_pessoas.dto.endereco;

import br.com.unicos.ms_pessoas.enums.TipoEndereco;

/**
 * DTO de retorno que representa um endereço completo associado a uma pessoa.
 */
public record EnderecoResponse(
        Long id,
        Long pessoaId,
        TipoEndereco tipo,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        Long municipioId,
        String cep,
        Boolean principal
) {}
