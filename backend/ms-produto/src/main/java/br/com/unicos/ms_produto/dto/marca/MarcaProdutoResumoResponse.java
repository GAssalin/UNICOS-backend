package br.com.unicos.ms_produto.dto.marca;

public record MarcaProdutoResumoResponse(
        Long id,
        String nome,
        Boolean ativo
) {}
