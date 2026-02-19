package br.com.unicos.ms_compras.dto.produtoatributo;

import jakarta.validation.constraints.NotBlank;

public record ProdutoAtributoUpdateRequest(
        @NotBlank String nome,
        String descricao
) {}
