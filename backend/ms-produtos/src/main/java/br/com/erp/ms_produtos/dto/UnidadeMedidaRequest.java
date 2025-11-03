package br.com.erp.ms_produtos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para criação e atualização de unidades de medida.
 */
public record UnidadeMedidaRequest(

        @NotBlank(message = "O nome da unidade é obrigatório.")
        @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres.")
        String nome,

        @NotBlank(message = "A sigla é obrigatória.")
        @Size(max = 10, message = "A sigla deve ter no máximo 10 caracteres.")
        String sigla
) {}