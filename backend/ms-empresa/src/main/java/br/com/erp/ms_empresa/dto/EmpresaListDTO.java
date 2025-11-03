package br.com.erp.ms_empresa.dto;

/**
 * DTO usado para listagem simplificada de empresas.
 */
public record EmpresaListDTO(
        Long id,
        String razaoSocial,
        String nomeFantasia,
        String cnpj
) {}