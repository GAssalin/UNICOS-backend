package br.com.unicos.ms_empresa.dto;

public record ContatoEmpresaResponse(
        Long id,
        String telefone,
        String email,
        Boolean ativo
) {}