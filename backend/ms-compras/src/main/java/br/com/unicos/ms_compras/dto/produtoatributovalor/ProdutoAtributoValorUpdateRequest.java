package br.com.unicos.ms_compras.dto.produtoatributovalor;

import jakarta.validation.constraints.NotBlank;

public record ProdutoAtributoValorUpdateRequest(
        @NotBlank String valor
) {}
