package br.com.unicos.ms_vendas.dto.produtocodigobarras;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoCodigoBarrasUpdateRequest(
        @NotBlank String codigoBarras,
        @NotNull Boolean principal
) {}
