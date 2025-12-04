package br.com.unicos.ms_produtos.dto.imagem_produto;

/**
 * DTO de retorno que representa uma imagem associada a um produto,
 * incluindo metadados e informações de exibição.
 */
public record ImagemProdutoResponse(
        Long id,
        Long produtoId,
        String url,
        String descricaoAlt,
        Boolean principal,
        Integer ordemExibicao,
        Boolean ativo
) {}
