package br.com.unicos.ms_produtos.dto.categoria;

/**
 * DTO utilizado em listagens de categorias,
 * trazendo apenas informações essenciais.
 */
public record CategoriaListDTO(
        Long id,
        String nome,
        Long categoriaPaiId,
        Boolean ativo
) {}
