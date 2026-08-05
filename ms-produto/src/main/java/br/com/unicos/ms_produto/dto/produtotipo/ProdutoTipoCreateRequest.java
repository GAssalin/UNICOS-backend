package br.com.unicos.ms_produto.dto.produtotipo;

import jakarta.validation.constraints.NotBlank;

public record ProdutoTipoCreateRequest(
        @NotBlank String nome,
        String descricao
) {}
