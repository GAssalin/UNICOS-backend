package br.com.unicos.ms_produto.dto.produtoatributovalor;

import jakarta.validation.constraints.NotBlank;

public record ProdutoAtributoValorUpdateRequest(
        @NotBlank String valor
) {}
