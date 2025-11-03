package br.com.erp.ms_produtos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para criação e atualização de atributos personalizados de produto.
 */
public record AtributoPersonalizadoRequest(

        @NotNull(message = "O ID do produto é obrigatório.")
        Long produtoId,

        @NotBlank(message = "O nome do atributo é obrigatório.")
        @Size(max = 100, message = "O nome do atributo deve ter no máximo 100 caracteres.")
        String nome,

        @NotBlank(message = "O valor do atributo é obrigatório.")
        @Size(max = 100, message = "O valor do atributo deve ter no máximo 100 caracteres.")
        String valor
) {}