package br.com.unicos.ms_compras.dto.produtoimagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoImagemCreateRequest(
        @NotNull Long produtoId,
        @NotBlank String url,
        String altTexto,
        @NotNull Boolean principal,
        @NotNull Integer ordem
) {}
