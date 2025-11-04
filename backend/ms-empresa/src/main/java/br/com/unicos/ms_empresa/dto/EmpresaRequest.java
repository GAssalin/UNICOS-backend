package br.com.unicos.ms_empresa.dto;

/**
 * DTOs referentes à entidade Empresa.
 */
public record EmpresaRequest(
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String inscricaoEstadual,
        String inscricaoMunicipal
) {}