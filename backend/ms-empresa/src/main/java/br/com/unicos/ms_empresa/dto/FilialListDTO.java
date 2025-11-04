package br.com.unicos.ms_empresa.dto;

public record FilialListDTO(
        Long id,
        String nome,
        String cnpj,
        Boolean ativo
) {}