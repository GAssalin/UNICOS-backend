package br.com.unicos.ms_pessoas.dto;

import java.time.LocalDate;

/**
 * DTO de retorno de colaboradores.
 */
public record ColaboradorResponse(
        Long id,
        Long pessoaId,
        Long empresaId,
        Long cargoId,
        Long departamentoId,
        LocalDate dataAdmissao,
        LocalDate dataDesligamento,
        String matricula,
        boolean ativo
) {}