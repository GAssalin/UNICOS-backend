package br.com.unicos.ms_produtos.dto.produto_variacao;

import java.math.BigDecimal;

/**
 * DTO utilizado em listagens de variações de produto,
 * retornando apenas informações essenciais para exibição.
 */
public record ProdutoVariacaoListDTO(
        Long id,
        String nome,
        String sku,
        BigDecimal preco,
        Boolean ativo
) {}
