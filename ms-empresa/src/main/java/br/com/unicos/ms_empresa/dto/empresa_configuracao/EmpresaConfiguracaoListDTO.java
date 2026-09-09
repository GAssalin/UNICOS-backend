package br.com.unicos.ms_empresa.dto.empresa_configuracao;

/**
 * DTO simplificado para listagem de configurações.
 */
public record EmpresaConfiguracaoListDTO(
        Long id,
        String chave,
        String valor
) { }
