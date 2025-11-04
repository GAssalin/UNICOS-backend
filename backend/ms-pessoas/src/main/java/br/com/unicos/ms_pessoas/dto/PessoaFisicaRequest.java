package br.com.unicos.ms_pessoas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação e atualização de pessoas físicas.
 */
public record PessoaFisicaRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150)
        String nome,

        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "O CPF deve estar no formato 000.000.000-00.")
        @NotBlank(message = "O CPF é obrigatório.")
        String cpf,

        @Size(max = 20)
        String rg,

        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "A data de nascimento deve estar no formato yyyy-MM-dd.")
        String dataNascimento,

        @Size(max = 20)
        String genero,

        boolean ativo
) { }