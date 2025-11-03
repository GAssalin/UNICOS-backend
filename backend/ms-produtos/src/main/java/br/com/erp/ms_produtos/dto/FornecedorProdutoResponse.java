package br.com.erp.ms_produtos.dto;

import java.math.BigDecimal;

/**
 * DTO usado para retorno detalhado de FornecedorProduto.
 */
public record FornecedorProdutoResponse(
        Long id,
        Long fornecedorId,
        Long produtoId,
        String produtoNome,
        BigDecimal precoCusto,
        Integer prazoEntregaDias
) {}