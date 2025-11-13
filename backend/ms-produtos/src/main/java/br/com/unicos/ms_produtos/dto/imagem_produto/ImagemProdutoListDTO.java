package br.com.unicos.ms_produtos.dto.imagem_produto;

/**
 * DTO utilizado em listagens de imagens do produto,
 * retornando apenas os campos essenciais.
 */
public record ImagemProdutoListDTO(
        Long id,
        String url,
        Boolean principal,
        Integer ordemExibicao
) {}
