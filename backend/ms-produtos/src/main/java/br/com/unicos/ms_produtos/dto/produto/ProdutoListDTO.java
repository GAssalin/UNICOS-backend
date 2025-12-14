package br.com.unicos.ms_produtos.dto.produto;

/**
 * DTO utilizado para listagem leve de produtos.
 * Ideal para catálogos e consultas rápidas.
 */
public record ProdutoListDTO(
        Long id,

        String nome,
        String sku,

        Boolean ativo,

        Long categoriaId,
        Long marcaId
) {}
