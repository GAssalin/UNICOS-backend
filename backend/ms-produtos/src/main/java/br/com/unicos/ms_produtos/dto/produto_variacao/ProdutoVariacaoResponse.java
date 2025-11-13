package br.com.unicos.ms_produtos.dto.produto_variacao;

import java.math.BigDecimal;

/**
 * DTO de retorno que representa uma variação completa de produto.
 *
 * <p>
 * Inclui identificação, dados básicos, atributos específicos e preço próprio.
 * </p>
 */
public record ProdutoVariacaoResponse(
        Long id,
        Long produtoId,
        String nome,
        String sku,
        BigDecimal preco,
        String codigoBarras,
        String cor,
        String tamanho,
        String material,
        Boolean ativo
) {}
