package br.com.unicos.ms_compras.dto.produtotipo;

import jakarta.validation.constraints.NotBlank;

public record ProdutoTipoCreateRequest(
        @NotBlank String nome,
        String descricao
) {}
