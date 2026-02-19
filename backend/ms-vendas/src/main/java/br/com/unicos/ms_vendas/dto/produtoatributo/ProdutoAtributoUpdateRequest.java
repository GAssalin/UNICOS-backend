package br.com.unicos.ms_vendas.dto.produtoatributo;

import jakarta.validation.constraints.NotBlank;

public record ProdutoAtributoUpdateRequest(
        @NotBlank String nome,
        String descricao
) {}
