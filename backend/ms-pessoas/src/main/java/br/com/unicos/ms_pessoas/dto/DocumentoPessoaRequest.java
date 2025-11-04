package br.com.unicos.ms_pessoas.dto;

import br.com.unicos.ms_pessoas.enums.TipoDocumento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * DTO utilizado para criação e atualização de documentos de pessoa.
 */
public record DocumentoPessoaRequest(

        @NotNull(message = "O ID da pessoa é obrigatório.")
        Long pessoaId,

        @NotNull(message = "O tipo de documento é obrigatório.")
        TipoDocumento tipoDocumento,

        @NotBlank @Size(max = 30)
        String numero,

        @Size(max = 30)
        String orgaoEmissor,

        @Size(max = 2)
        String ufEmissor,

        LocalDate dataEmissao,

        LocalDate dataValidade,

        @Size(max = 255)
        String arquivoId,

        boolean ativo
) {}