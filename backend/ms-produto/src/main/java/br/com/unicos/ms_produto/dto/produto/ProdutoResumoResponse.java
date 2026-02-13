package br.com.unicos.ms_produto.dto.produto;

import br.com.unicos.ms_produto.enums.TipoProduto;

import java.math.BigDecimal;

/**
 * DTO reduzido para listagens de {@code Produto}.
 */
public record ProdutoResumoResponse(

        Long id,
        String codigo,
        String nome,
        TipoProduto tipoProduto,
        BigDecimal precoBase,
        Boolean ativo

) {}
