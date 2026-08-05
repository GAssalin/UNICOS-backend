package br.com.unicos.ms_pessoas.dto.endereco;

import br.com.unicos.ms_pessoas.enums.TipoEndereco;

/**
 * DTO utilizado em listagens de endereços,
 * trazendo apenas informações essenciais.
 */
public record EnderecoListDTO(
        Long id,
        TipoEndereco tipo,
        String logradouro,
        String numero,
        String bairro,
        String cep,
        Boolean principal
) {}
