package br.com.unicos.ms_vendas.dto.categoria;

public record CategoriaProdutoResumoResponse(
        Long id,
        String nome,
        Boolean ativo
) {}
