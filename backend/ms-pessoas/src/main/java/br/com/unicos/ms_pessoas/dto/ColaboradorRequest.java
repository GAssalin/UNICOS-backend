package br.com.unicos.ms_pessoas.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO utilizado para criação e atualização de colaboradores.
 */
public record ColaboradorRequest(

        @NotNull(message = "O ID da pessoa é obrigatório.")
        Long pessoaId,

        @NotNull(message = "O ID da empresa é obrigatório.")
        Long empresaId,

        Long cargoId,

        @NotNull(message = "O ID do departamento é obrigatório.")
        Long departamentoId,

        LocalDate dataAdmissao,

        LocalDate dataDesligamento,

        String matricula,

        boolean ativo
) {}