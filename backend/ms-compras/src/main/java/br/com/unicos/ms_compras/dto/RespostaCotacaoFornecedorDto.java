package br.com.unicos.ms_compras.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta de fornecedor para cotação.
 */
public record RespostaCotacaoFornecedorDto(
        Long id,
        Long cotacaoCompraId,
        Long fornecedorId,
        String status,
        LocalDateTime respondidoEm,
        Long condicaoPagamentoId,
        BigDecimal totalProposto,
        BigDecimal frete,
        String observacao
) {
}
