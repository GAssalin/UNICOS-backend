package br.com.unicos.ms_vendas.dto.marca;

public record MarcaProdutoResumoResponse(
        Long id,
        String nome,
        Boolean ativo
) {}
