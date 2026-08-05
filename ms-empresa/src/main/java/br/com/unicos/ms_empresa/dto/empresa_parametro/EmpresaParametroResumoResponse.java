package br.com.unicos.ms_empresa.dto.empresa_parametro;

/**
 * DTO simplificado para listagem de parâmetros da empresa.
 */
public record EmpresaParametroResumoResponse(
        Long id,
        String chave,
        String valor
) { }
