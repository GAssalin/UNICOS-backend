package br.com.unicos.ms_produtos.dto.produto_atributo;

/**
 * DTO utilizado para listagens de atributos aplicados ao produto,
 * retornando informações essenciais.
 */
public record ProdutoAtributoValorListDTO(
        Long id,
        String atributoNome,
        String valor
) {}
