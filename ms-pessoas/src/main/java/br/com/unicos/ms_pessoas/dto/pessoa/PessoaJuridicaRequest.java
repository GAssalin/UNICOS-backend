package br.com.unicos.ms_pessoas.dto.pessoa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de Pessoas Jurídicas.
 * <p>
 * Inclui os campos herdados de Pessoa e os específicos de Pessoa Jurídica.
 */
public record PessoaJuridicaRequest(

        @NotBlank(message = "O nome da empresa é obrigatório.")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
        String nome,

        @NotBlank(message = "O CNPJ é obrigatório.")
        @Size(min = 14, max = 14, message = "O CNPJ deve conter 14 dígitos, sem formatação.")
        String cnpj,

        @NotBlank(message = "A razão social é obrigatória.")
        @Size(max = 200, message = "A razão social deve ter no máximo 200 caracteres.")
        String razaoSocial,

        @Size(max = 200, message = "O nome fantasia deve ter no máximo 200 caracteres.")
        String nomeFantasia
) {}
