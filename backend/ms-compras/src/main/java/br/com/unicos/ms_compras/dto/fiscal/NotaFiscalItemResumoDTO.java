package br.com.unicos.ms_compras.dto.fiscal;

import java.math.BigDecimal;

/**
 * DTO resumido dos itens de uma nota fiscal de compra.
 *
 * <p>Utilizado em listagens e respostas detalhadas para reduzir carga de dados.</p>
 */
public record NotaFiscalItemResumoDTO(

        Long id,
        Long produtoId,
        String descricaoProduto,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal,
        BigDecimal valorICMS,
        BigDecimal valorIPI,
        BigDecimal valorDesconto
) {}
