package br.com.unicos.ms_produtos.dto.categoria;

/**
 * DTO utilizado em listagens simplificadas de categorias.
 */
public record CategoriaListDTO(
        Long id,
        String nome,
        Long categoriaPaiId,
        Boolean ativo
) {}
