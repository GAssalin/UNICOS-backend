package br.com.unicos.ms_empresa.dto;

public record EmpresaListDTO(
        Long id,
        String nomeFantasia,
        String cnpj
) {}