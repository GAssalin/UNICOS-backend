package br.com.unicos.ms_produtos.dto.produto_atributo;

/**
 * DTO de retorno que representa um valor atribuído a um produto
 * para um atributo personalizado específico.
 */
public record ProdutoAtributoValorResponse(
        Long id,
        Long produtoId,
        Long atributoId,
        String atributoNome,
        String valor
) {}
