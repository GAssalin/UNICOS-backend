package br.com.unicos.ms_compras.dto.marca;

import jakarta.validation.constraints.NotBlank;

public record MarcaProdutoCreateRequest(
        @NotBlank String nome,
        String descricao
) {}
