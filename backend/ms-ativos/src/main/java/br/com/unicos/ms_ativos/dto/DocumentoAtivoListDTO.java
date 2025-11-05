package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.StatusDocumento;
import br.com.unicos.ms_ativos.enums.TipoDocumentoAtivo;

import java.time.LocalDate;

/**
 * DTO simplificado para listagem de documentos vinculados a ativos.
 * <p>
 * Usado em telas de listagem e relatórios resumidos.
 */
public record DocumentoAtivoListDTO(

        /** Identificador único do documento. */
        Long id,

        /** Tipo do documento. */
        TipoDocumentoAtivo tipo,

        /** Número do documento. */
        String numero,

        /** Data de emissão. */
        LocalDate dataEmissao,

        /** Status atual do documento. */
        StatusDocumento status
) { }
