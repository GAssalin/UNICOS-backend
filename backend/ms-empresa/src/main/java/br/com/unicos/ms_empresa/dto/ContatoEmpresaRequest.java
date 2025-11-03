package br.com.unicos.ms_empresa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para criação e atualização de contatos vinculados à empresa.
 */
public record ContatoEmpresaRequest(

        @NotNull(message = "O ID da empresa é obrigatório.")
        Long empresaId,

        @NotBlank(message = "O nome do contato é obrigatório.")
        @Size(max = 100, message = "O nome do contato deve ter no máximo 100 caracteres.")
        String nomeContato,

        @Size(max = 50, message = "O cargo deve ter no máximo 50 caracteres.")
        String cargo,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
        String telefone,

        @Size(max = 20, message = "O celular deve ter no máximo 20 caracteres.")
        String celular,

        @Email(message = "O e-mail informado não é válido.")
        @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
        String email
) {}