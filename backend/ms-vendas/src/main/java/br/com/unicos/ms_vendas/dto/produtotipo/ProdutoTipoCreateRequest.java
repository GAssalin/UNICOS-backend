package br.com.unicos.ms_vendas.dto.produtotipo;

import jakarta.validation.constraints.NotBlank;

public record ProdutoTipoCreateRequest(
        @NotBlank String nome,
        String descricao
) {}
