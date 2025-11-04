package br.com.unicos.ms_empresa.dto;

public record DepartamentoListDTO(
        Long id,
        String nome,
        Boolean ativo
) {}