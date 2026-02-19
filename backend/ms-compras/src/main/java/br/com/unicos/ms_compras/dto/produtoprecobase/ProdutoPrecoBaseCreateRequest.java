package br.com.unicos.ms_compras.dto.produtoprecobase;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoPrecoBaseCreateRequest(
        @NotNull Long produtoId,
        BigDecimal custoBase,
        BigDecimal precoVendaBase,
        BigDecimal margemBase
) {}
