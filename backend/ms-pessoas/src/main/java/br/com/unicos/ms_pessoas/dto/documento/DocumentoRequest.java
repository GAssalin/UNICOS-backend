package br.com.unicos.ms_pessoas.dto.documento;

import br.com.unicos.ms_pessoas.enums.TipoDocumento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO utilizado para criação ou atualização de documentos de uma pessoa.
 * <p>
 * Permite definir tipo, número, órgão emissor e data de emissão.
 */
public record DocumentoRequest(

        @NotNull(message = "O ID da pessoa é obrigatório.")
        Long pessoaId,

        @NotNull(message = "O tipo do documento é obrigatório.")
        TipoDocumento tipo,

        @NotBlank(message = "O número do documento é obrigatório.")
        @Size(max = 50, message = "O número do documento deve ter no máximo 50 caracteres.")
        String numero,

        @Size(max = 50, message = "O órgão emissor deve ter no máximo 50 caracteres.")
        String orgaoEmissor,

        LocalDate dataEmissao
) {}
