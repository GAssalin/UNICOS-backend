package br.com.unicos.ms_compras.dto.produtoprecobase;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoPrecoBaseResponse(
        Long id,
        Long produtoId,
        BigDecimal custoBase,
        BigDecimal precoVendaBase,
        BigDecimal margemBase,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime criadoEm,
        Long atualizadoPor,
        LocalDateTime atualizadoEm
) {}
