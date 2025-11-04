package br.com.unicos.ms_empresa.dto;

public record SetorResponse(
        Long id,
        String nome,
        Boolean ativo
) {}