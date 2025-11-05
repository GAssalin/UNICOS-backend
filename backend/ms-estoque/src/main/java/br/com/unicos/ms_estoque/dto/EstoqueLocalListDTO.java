package br.com.unicos.ms_estoque.dto;

import br.com.unicos.ms_estoque.enums.TipoLocalEstoque;

/**
 * DTO resumido para listagens de locais de estoque.
 */
public record EstoqueLocalListDTO(
        Long id,
        String nome,
        TipoLocalEstoque tipo
) {}
