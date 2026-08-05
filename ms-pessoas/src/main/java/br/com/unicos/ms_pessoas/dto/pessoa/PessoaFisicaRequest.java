package br.com.unicos.ms_pessoas.dto.pessoa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO utilizado para criação ou atualização de Pessoas Físicas.
 * <p>
 * Inclui os campos herdados de Pessoa e os específicos de Pessoa Física.
 */
public record PessoaFisicaRequest(

        @NotBlank(message = "O nome da pessoa é obrigatório.")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
        String nome,

        @NotBlank(message = "O CPF é obrigatório.")
        @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 dígitos, sem formatação.")
        String cpf,

        @NotNull(message = "A data de nascimento é obrigatória.")
        LocalDate dataNascimento,

        @Size(max = 150, message = "O nome social deve ter no máximo 150 caracteres.")
        String nomeSocial
) {}
