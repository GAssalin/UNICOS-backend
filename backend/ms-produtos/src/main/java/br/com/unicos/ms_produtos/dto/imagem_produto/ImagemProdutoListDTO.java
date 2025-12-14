package br.com.unicos.ms_produtos.dto.imagem_produto;

/**
 * DTO utilizado em listagens de imagens de produto,
 * retornando apenas informações essenciais para exibição.
 */
public record ImagemProdutoListDTO(
        Long id,
        String url,
        Boolean principal,
        Integer ordemExibicao
) {}
