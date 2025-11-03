package br.com.erp.ms_empresa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para criação e atualização das filiais de uma empresa.
 */
public record FilialRequest(

        @NotNull(message = "O ID da empresa matriz é obrigatório.")
        Long empresaId,

        Boolean matriz,

        @NotBlank(message = "A razão social da filial é obrigatória.")
        @Size(max = 150, message = "A razão social deve ter no máximo 150 caracteres.")
        String razaoSocial,

        @NotBlank(message = "O nome fantasia da filial é obrigatório.")
        @Size(max = 150, message = "O nome fantasia deve ter no máximo 150 caracteres.")
        String nomeFantasia,

        @NotBlank(message = "O CNPJ é obrigatório.")
        @Size(max = 18, message = "O CNPJ deve ter no máximo 18 caracteres.")
        String cnpj,

        @Size(max = 20, message = "A inscrição estadual deve ter no máximo 20 caracteres.")
        String inscricaoEstadual,

        @Size(max = 20, message = "A inscrição municipal deve ter no máximo 20 caracteres.")
        String inscricaoMunicipal,

        @Size(max = 50, message = "O telefone deve ter no máximo 50 caracteres.")
        String telefone,

        @Email(message = "O e-mail informado não é válido.")
        @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
        String email,

        @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres.")
        String endereco,

        @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres.")
        String cidade,

        @Size(max = 2, message = "A UF deve ter no máximo 2 caracteres.")
        String uf,

        @Size(max = 10, message = "O CEP deve ter no máximo 10 caracteres.")
        String cep
) {}