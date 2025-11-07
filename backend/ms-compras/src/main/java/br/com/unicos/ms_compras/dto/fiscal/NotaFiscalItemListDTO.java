package br.com.unicos.ms_compras.dto.fiscal;

import java.math.BigDecimal;

/**
 * DTO utilizado para listagem de itens de Nota Fiscal de Compra.
 *
 * <p>Fornece uma visão resumida para exibição em tabelas e consultas rápidas.</p>
 */
public record NotaFiscalItemListDTO(

        Long id,
        Long produtoId,
        String descricaoProduto,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal
) {}
