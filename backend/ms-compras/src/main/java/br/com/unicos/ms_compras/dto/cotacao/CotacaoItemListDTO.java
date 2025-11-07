package br.com.unicos.ms_compras.dto.cotacao;

import java.math.BigDecimal;

/**
 * DTO utilizado para listagem de itens de cotação.
 *
 * <p>Fornece uma visão resumida para consultas e exibições em tabelas.</p>
 */
public record CotacaoItemListDTO(

        Long id,
        Long produtoId,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal
) {}
