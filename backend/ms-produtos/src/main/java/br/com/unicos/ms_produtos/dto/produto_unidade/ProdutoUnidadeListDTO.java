package br.com.unicos.ms_produtos.dto.produto_unidade;

/**
 * DTO utilizado em listagens simples de vínculos entre produto
 * e unidade de medida.
 */
public record ProdutoUnidadeListDTO(
        Long id,
        String produtoNome,
        String unidadeMedidaNome,
        Double quantidadePadrao
) {}
