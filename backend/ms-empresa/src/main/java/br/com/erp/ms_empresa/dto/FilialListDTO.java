package br.com.erp.ms_empresa.dto;

/**
 * DTO usado para listagem simplificada das filiais de uma empresa.
 */
public record FilialListDTO(
        Long id,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String cidade,
        String uf
) {}