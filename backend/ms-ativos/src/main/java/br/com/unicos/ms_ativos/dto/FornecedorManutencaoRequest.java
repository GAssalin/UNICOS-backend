package br.com.unicos.ms_ativos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO utilizado para criação ou atualização de fornecedores de manutenção.
 * <p>
 * Contém os dados cadastrais básicos do fornecedor.
 */
public record FornecedorManutencaoRequest(

        /** Nome ou razão social do fornecedor. */
        @NotBlank(message = "O nome do fornecedor é obrigatório.")
        String nome,

        /** CNPJ do fornecedor. */
        @NotBlank(message = "O CNPJ é obrigatório.")
        @Pattern(regexp = "\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}", message = "O CNPJ informado é inválido.")
        String cnpj,

        /** Telefone de contato do fornecedor. */
        String telefone,

        /** E-mail de contato principal. */
        @Email(message = "O e-mail informado é inválido.")
        String email,

        /** Nome do responsável técnico ou comercial. */
        String responsavel
) { }
