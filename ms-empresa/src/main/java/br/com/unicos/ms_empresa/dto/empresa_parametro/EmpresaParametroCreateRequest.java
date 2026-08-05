package br.com.unicos.ms_empresa.dto.empresa_parametro;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilizado para criação de um parâmetro da empresa.
 */
public record EmpresaParametroCreateRequest(
        @NotBlank
        String chave,
        @NotBlank
        String valor
) { }