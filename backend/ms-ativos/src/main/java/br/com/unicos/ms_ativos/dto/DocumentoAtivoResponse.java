package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.StatusDocumento;
import br.com.unicos.ms_ativos.enums.TipoDocumentoAtivo;

import java.time.LocalDate;

/**
 * DTO de resposta utilizado para exibir informações detalhadas
 * de um documento associado a um ativo.
 */
public record DocumentoAtivoResponse(

        /** Identificador único do documento. */
        Long id,

        /** Identificador do ativo vinculado ao documento. */
        Long ativoId,

        /** Nome do ativo (para exibição em relatórios). */
        String nomeAtivo,

        /** Tipo do documento (nota fiscal, garantia, laudo técnico, etc.). */
        TipoDocumentoAtivo tipo,

        /** Número de identificação do documento. */
        String numero,

        /** Data de emissão do documento. */
        LocalDate dataEmissao,

        /** URL ou caminho do arquivo digitalizado. */
        String arquivoUrl,

        /** Status atual do documento. */
        StatusDocumento status,

        /** Observações gerais sobre o documento. */
        String observacao
) { }
