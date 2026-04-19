package br.com.unicos.ms_empresa.dto.empresa_configuracao;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilizado para criação de uma configuração da empresa.
 */
public record EmpresaConfiguracaoCreateRequest(
        @NotBlank
        String chave,
        @NotBlank
        String valor
) { }