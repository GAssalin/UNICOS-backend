package br.com.erp.ms_produtos.dto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        String sku,
        String categoriaNome,
        String marcaNome,
        Boolean ativo
) {
}