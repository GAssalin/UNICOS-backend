package br.com.unicos.ms_compras.dto.produtotipo;

import jakarta.validation.constraints.NotBlank;

public record ProdutoTipoUpdateRequest(
        @NotBlank String nome,
        String descricao
) {}
