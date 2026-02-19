package br.com.unicos.ms_vendas.dto.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaProdutoUpdateRequest(
        @NotBlank String nome,
        String descricao,
        Long categoriaPaiId
) {}
