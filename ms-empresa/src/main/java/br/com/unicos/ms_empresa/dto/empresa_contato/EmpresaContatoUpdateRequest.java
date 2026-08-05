package br.com.unicos.ms_empresa.dto.empresa_contato;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilizado para atualização de um contato institucional da empresa.
 */
public record EmpresaContatoUpdateRequest(
        @NotBlank
        String valor,
        boolean principal
) { }
