package br.com.unicos.ms_compras.dto;

import java.math.BigDecimal;

/**
 * DTO de parcela da condição de pagamento.
 */
public record CondicaoPagamentoParcelaDto(
        Long id,
        Long condicaoPagamentoId,
        Integer ordem,
        Integer diasAposEmissao,
        BigDecimal percentual
) { }
