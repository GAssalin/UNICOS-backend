package br.com.unicos.ms_pessoas.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO utilizado para criação e atualização das relações entre pessoas.
 */
public record PessoaRelacaoRequest(

        @NotNull(message = "O ID da pessoa é obrigatório.")
        Long pessoaId,

        @NotNull(message = "O tipo de relação é obrigatório.")
        Long tipoRelacaoId,

        LocalDate dataInicio,

        LocalDate dataFim,

        boolean ativo
) {}