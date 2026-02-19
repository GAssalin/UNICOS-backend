package br.com.unicos.ms_vendas.dto.marca;

import jakarta.validation.constraints.NotBlank;

public record MarcaProdutoCreateRequest(
        @NotBlank String nome,
        String descricao
) {}
