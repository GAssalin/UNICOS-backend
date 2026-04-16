package br.com.unicos.ms_empresa.dto.empresa_configuracao;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilizado para atualização de uma configuração da empresa.
 */
public record EmpresaConfiguracaoUpdateRequest(
        @NotBlank
        String chave,
        @NotBlank
        String valor
) { }
