package br.com.unicos.ms_compras.dto;

import java.math.BigDecimal;

/**
 * DTO de item de cotação de compra.
 */
public record ItemCotacaoDto(
        Long id,
        Long cotacaoCompraId,
        Long produtoId,
        String produtoDescricaoSnapshot,
        String unidadeSnapshot,
        BigDecimal quantidade,
        String observacao
) {
}
