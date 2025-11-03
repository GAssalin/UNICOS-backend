package br.com.erp.ms_produtos.dto;

import java.math.BigDecimal;

/**
 * DTO usado para listagem simplificada de fornecedores vinculados a produtos.
 */
public record FornecedorProdutoListDTO(
        Long id,
        Long fornecedorId,
        String produtoNome,
        BigDecimal precoCusto
) {}