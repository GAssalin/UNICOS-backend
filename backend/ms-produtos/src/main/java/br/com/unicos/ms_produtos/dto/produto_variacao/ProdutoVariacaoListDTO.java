package br.com.unicos.ms_produtos.dto.produto_variacao;

import java.math.BigDecimal;

/**
 * DTO utilizado em listagens de variações,
 * trazendo somente os dados essenciais para exibição.
 */
public record ProdutoVariacaoListDTO(
        Long id,
        String nome,
        String sku,
        BigDecimal preco,
        Boolean ativo
) {}
