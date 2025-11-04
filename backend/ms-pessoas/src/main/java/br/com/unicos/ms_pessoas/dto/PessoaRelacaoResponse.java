package br.com.unicos.ms_pessoas.dto;

import java.time.LocalDate;

/**
 * DTO de retorno das relações entre pessoas.
 */
public record PessoaRelacaoResponse(
        Long id,
        Long pessoaId,
        Long tipoRelacaoId,
        LocalDate dataInicio,
        LocalDate dataFim,
        boolean ativo
) {}