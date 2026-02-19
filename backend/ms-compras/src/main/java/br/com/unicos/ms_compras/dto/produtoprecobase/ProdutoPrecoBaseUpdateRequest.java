package br.com.unicos.ms_compras.dto.produtoprecobase;

import java.math.BigDecimal;

public record ProdutoPrecoBaseUpdateRequest(
        BigDecimal custoBase,
        BigDecimal precoVendaBase,
        BigDecimal margemBase
) {}
