package br.com.unicos.ms_pessoas.dto.contato;

import br.com.unicos.ms_pessoas.enums.TipoContato;

/**
 * DTO utilizado em listagens de contatos,
 * trazendo apenas informações essenciais.
 */
public record ContatoListDTO(
        Long id,
        TipoContato tipo,
        String valor,
        Boolean principal
) {}
