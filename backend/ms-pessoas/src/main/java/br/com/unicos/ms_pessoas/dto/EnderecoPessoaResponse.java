package br.com.unicos.ms_pessoas.dto;

import br.com.unicos.ms_pessoas.enums.TipoEndereco;
import br.com.unicos.ms_pessoas.enums.TipoLogradouro;

/**
 * DTO de retorno para endereços de pessoa.
 */
public record EnderecoPessoaResponse(
        Long id,
        Long pessoaId,
        TipoEndereco tipoEndereco,
        TipoLogradouro tipoLogradouro,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cep,
        Long municipioId,
        boolean principal
) {}