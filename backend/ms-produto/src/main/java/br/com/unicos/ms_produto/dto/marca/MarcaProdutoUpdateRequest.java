package br.com.unicos.ms_produto.dto.marca;

import jakarta.validation.constraints.NotBlank;

public record MarcaProdutoUpdateRequest(
        @NotBlank String nome,
        String descricao
) {}
