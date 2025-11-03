package br.com.erp.ms_empresa.dto;

/**
 * DTO usado para listagem simplificada dos departamentos de uma empresa.
 */
public record DepartamentoEmpresaListDTO(
        Long id,
        String nome,
        String descricao,
        Boolean ativo
) {}