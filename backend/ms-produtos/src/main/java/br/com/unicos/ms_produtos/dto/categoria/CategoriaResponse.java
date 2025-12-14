package br.com.unicos.ms_produtos.dto.categoria;

import java.util.List;

/**
 * DTO de retorno que representa uma categoria completa,
 * incluindo hierarquia, status e subcategorias.
 */
public record CategoriaResponse(
        Long id,
        String nome,
        String descricao,
        Long categoriaPaiId,
        Boolean ativo,
        List<CategoriaListDTO> subcategorias
) {}
