package br.com.unicos.ms_produtos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para criação e atualização de atributos personalizados de categoria.
 */
public record AtributoPersonalizadoRequest(

        @NotNull(message = "O ID da categoria é obrigatório.")
        Long categoriaId,

        @NotBlank(message = "O nome do atributo é obrigatório.")
        @Size(max = 100, message = "O nome do atributo deve ter no máximo 100 caracteres.")
        String nome
) {}
