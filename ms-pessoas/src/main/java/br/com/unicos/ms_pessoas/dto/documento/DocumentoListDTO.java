package br.com.unicos.ms_pessoas.dto.documento;

import br.com.unicos.ms_pessoas.enums.TipoDocumento;

/**
 * DTO utilizado em listagens de documentos,
 * trazendo apenas informações essenciais.
 */
public record DocumentoListDTO(
        Long id,
        TipoDocumento tipo,
        String numero
) {}
