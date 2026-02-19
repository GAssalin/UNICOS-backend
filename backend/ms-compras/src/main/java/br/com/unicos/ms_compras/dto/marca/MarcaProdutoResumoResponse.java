package br.com.unicos.ms_compras.dto.marca;

public record MarcaProdutoResumoResponse(
        Long id,
        String nome,
        Boolean ativo
) {}
