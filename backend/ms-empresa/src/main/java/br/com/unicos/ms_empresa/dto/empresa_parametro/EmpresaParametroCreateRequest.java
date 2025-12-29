package br.com.unicos.ms_empresa.dto.empresa_parametro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação de um parâmetro da empresa.
 */
public record EmpresaParametroCreateRequest(
        @NotNull
        Long empresaId,
        @NotNull
        Long empresaRefId,
        @NotBlank
        String chave,
        @NotBlank
        String valor
) { }
