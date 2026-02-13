package br.com.unicos.ms_produto.dto.produtoimagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoImagemUpdateRequest(
        @NotBlank String url,
        String altTexto,
        @NotNull Boolean principal,
        @NotNull Integer ordem
) {}
