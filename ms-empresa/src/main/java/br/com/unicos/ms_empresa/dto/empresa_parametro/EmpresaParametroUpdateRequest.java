package br.com.unicos.ms_empresa.dto.empresa_parametro;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilizado para atualização de um parâmetro da empresa.
 */
public record EmpresaParametroUpdateRequest(
        @NotBlank
        String valor
) { }
