package br.com.unicos.ms_empresa.dto;

public record DepartamentoRequest(
        String nome,
        Long empresaId,
        Boolean ativo
) {}