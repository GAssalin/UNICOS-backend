package br.com.unicos.ms_empresa.dto.empresa_configuracao;

/**
 * DTO simplificado para listagem de configurações.
 */
public record EmpresaConfiguracaoResumoResponse(
        Long id,
        String chave,
        String valor
) { }
