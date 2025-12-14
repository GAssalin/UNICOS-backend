package br.com.unicos.ms_produtos.dto.produto_atributo;

/**
 * DTO utilizado em listagens de atributos aplicados a um produto,
 * retornando apenas informações essenciais para exibição.
 */
public record ProdutoAtributoValorListDTO(
        Long id,
        String atributoNome,
        String valor
) {}
