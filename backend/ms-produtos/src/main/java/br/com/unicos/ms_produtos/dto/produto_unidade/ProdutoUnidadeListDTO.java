package br.com.unicos.ms_produtos.dto.produto_unidade;

/**
 * DTO usado para listagem simples de vínculos produto–unidade de medida.
 */
public record ProdutoUnidadeListDTO(
        Long id,
        String produtoNome,
        String unidadeMedidaNome,
        Double quantidadePadrao
) {}