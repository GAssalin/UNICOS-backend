package br.com.unicos.ms_compras.dto.cotacao;

import java.math.BigDecimal;

/**
 * DTO resumido que representa um item cotado por um fornecedor.
 *
 * <p>Utilizado em respostas e listagens de propostas de fornecedores.</p>
 */
public record CotacaoItemResumoDTO(

        Long id,
        Long produtoId,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal
) {}
