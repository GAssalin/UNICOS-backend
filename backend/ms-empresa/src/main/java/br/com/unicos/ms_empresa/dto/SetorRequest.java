package br.com.unicos.ms_empresa.dto;

public record SetorRequest(
        String nome,
        Long departamentoId,
        Boolean ativo
) {}