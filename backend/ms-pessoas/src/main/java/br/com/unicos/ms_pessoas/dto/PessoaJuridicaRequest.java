package br.com.unicos.ms_pessoas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação e atualização de pessoas jurídicas.
 */
public record PessoaJuridicaRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150)
        String nome,

        @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "O CNPJ deve estar no formato 00.000.000/0000-00.")
        @NotBlank(message = "O CNPJ é obrigatório.")
        String cnpj,

        @Size(max = 150)
        String nomeFantasia,

        @Size(max = 30)
        String inscricaoEstadual,

        @Size(max = 30)
        String inscricaoMunicipal,

        boolean ativo
) {}