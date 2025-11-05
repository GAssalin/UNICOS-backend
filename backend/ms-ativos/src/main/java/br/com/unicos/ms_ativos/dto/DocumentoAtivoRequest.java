package br.com.unicos.ms_ativos.dto;

import br.com.unicos.ms_ativos.enums.StatusDocumento;
import br.com.unicos.ms_ativos.enums.TipoDocumentoAtivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * DTO utilizado para requisições de criação ou atualização
 * de documentos vinculados a um ativo.
 */
public record DocumentoAtivoRequest(

        /** Identificador do ativo ao qual o documento pertence. */
        @NotNull(message = "O ID do ativo é obrigatório.")
        Long ativoId,

        /** Tipo do documento (nota fiscal, garantia, laudo técnico, etc.). */
        @NotNull(message = "O tipo do documento é obrigatório.")
        TipoDocumentoAtivo tipo,

        /** Número de identificação do documento. */
        @NotBlank(message = "O número do documento é obrigatório.")
        String numero,

        /** Data de emissão do documento. */
        @NotNull(message = "A data de emissão é obrigatória.")
        @PastOrPresent(message = "A data de emissão não pode estar no futuro.")
        LocalDate dataEmissao,

        /** URL ou caminho do arquivo digitalizado. */
        String arquivoUrl,

        /** Status atual do documento (válido, vencido, etc.). */
        @NotNull(message = "O status do documento é obrigatório.")
        StatusDocumento status,

        /** Observações gerais sobre o documento. */
        String observacao
) { }
