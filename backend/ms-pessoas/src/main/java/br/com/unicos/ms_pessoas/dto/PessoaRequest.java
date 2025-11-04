package br.com.unicos.ms_pessoas.dto;

import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação e atualização de pessoas.
 */
public record PessoaRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150)
        String nome,

        @NotNull(message = "O tipo de pessoa é obrigatório.")
        TipoPessoa tipoPessoa,

        boolean ativo
) {}