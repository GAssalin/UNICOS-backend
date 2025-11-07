package br.com.unicos.ms_compras.dto.fiscal;

import java.math.BigDecimal;

/**
 * DTO de resposta detalhada do item da Nota Fiscal de Compra.
 *
 * <p>Inclui as informações completas de produto, quantidades e tributos.</p>
 */
public record NotaFiscalItemResponse(

        Long id,
        Long produtoId,
        String descricaoProduto,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal,
        BigDecimal valorICMS,
        BigDecimal valorIPI,
        BigDecimal valorDesconto,
        Long notaFiscalCompraId
) {}
