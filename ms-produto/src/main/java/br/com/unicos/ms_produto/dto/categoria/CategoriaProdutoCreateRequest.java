package br.com.unicos.ms_produto.dto.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaProdutoCreateRequest(
        @NotBlank String nome,
        String descricao,
        Long categoriaPaiId
) {}
