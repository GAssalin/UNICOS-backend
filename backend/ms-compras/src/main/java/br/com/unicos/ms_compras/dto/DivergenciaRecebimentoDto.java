package br.com.unicos.ms_compras.dto;

import java.math.BigDecimal;

/**
 * DTO de divergência de recebimento.
 */
public record DivergenciaRecebimentoDto(
        Long id,
        Long itemRecebimentoCompraId,
        String tipo,
        String descricao,
        BigDecimal quantidadeDivergente
) {
}
