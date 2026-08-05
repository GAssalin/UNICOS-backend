package br.com.unicos.ms_pessoas.dto.pessoa;

import br.com.unicos.ms_pessoas.enums.TipoPessoa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO base utilizado para criação ou atualização de pessoas.
 * <p>
 * Deve ser estendido ou utilizado em conjunto com DTOs específicos
 * para Pessoa Física e Pessoa Jurídica.
 */
public record PessoaRequest(

        @NotBlank(message = "O nome da pessoa é obrigatório.")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
        String nome,

        @NotNull(message = "O tipo da pessoa é obrigatório.")
        TipoPessoa tipoPessoa
) {}
