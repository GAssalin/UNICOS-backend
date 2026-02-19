package br.com.unicos.ms_vendas.dto.produtoimagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoImagemCreateRequest(
        @NotNull Long produtoId,
        @NotBlank String url,
        String altTexto,
        @NotNull Boolean principal,
        @NotNull Integer ordem
) {}
