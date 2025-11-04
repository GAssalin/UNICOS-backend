package br.com.unicos.ms_pessoas.dto;

import br.com.unicos.ms_pessoas.enums.TipoDocumento;
import java.time.LocalDate;

/**
 * DTO de retorno de documentos de pessoa.
 */
public record DocumentoPessoaResponse(
        Long id,
        Long pessoaId,
        TipoDocumento tipoDocumento,
        String numero,
        String orgaoEmissor,
        String ufEmissor,
        LocalDate dataEmissao,
        LocalDate dataValidade,
        String arquivoId,
        boolean ativo
) {}