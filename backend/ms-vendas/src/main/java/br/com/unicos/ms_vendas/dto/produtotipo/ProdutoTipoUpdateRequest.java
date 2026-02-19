package br.com.unicos.ms_vendas.dto.produtotipo;

import jakarta.validation.constraints.NotBlank;

public record ProdutoTipoUpdateRequest(
        @NotBlank String nome,
        String descricao
) {}
