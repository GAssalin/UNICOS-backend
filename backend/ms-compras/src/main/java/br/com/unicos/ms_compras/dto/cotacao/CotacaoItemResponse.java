package br.com.unicos.ms_compras.dto.cotacao;

import java.math.BigDecimal;

/**
 * DTO de resposta detalhada do item de cotação.
 *
 * <p>Inclui informações do produto, quantidade, valores e vínculo com a proposta do fornecedor.</p>
 */
public record CotacaoItemResponse(

        Long id,
        Long produtoId,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal,
        Long cotacaoFornecedorId
) {}
