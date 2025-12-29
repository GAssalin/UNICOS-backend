package br.com.unicos.ms_empresa.dto.empresa_configuracao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação de uma configuração da empresa (tenant).
 */
public record EmpresaConfiguracaoCreateRequest(
        @NotNull
        Long empresaId,
        @NotNull
        Long empresaRefId,
        @NotBlank
        String chave,
        @NotBlank
        String valor
) { }
