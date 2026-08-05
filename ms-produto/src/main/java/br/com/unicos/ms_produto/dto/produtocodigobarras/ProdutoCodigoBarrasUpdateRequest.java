package br.com.unicos.ms_produto.dto.produtocodigobarras;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoCodigoBarrasUpdateRequest(
        @NotBlank String codigoBarras,
        @NotNull Boolean principal
) {}
