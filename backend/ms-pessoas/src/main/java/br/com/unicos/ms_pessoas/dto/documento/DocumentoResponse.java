package br.com.unicos.ms_pessoas.dto.documento;

import br.com.unicos.ms_pessoas.enums.TipoDocumento;

import java.time.LocalDate;

/**
 * DTO de retorno que representa um documento completo associado a uma pessoa.
 */
public record DocumentoResponse(
        Long id,
        Long pessoaId,
        TipoDocumento tipo,
        String numero,
        String orgaoEmissor,
        LocalDate dataEmissao
) {}
