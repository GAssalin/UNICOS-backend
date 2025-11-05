package br.com.unicos.ms_estoque.dto;

import br.com.unicos.ms_estoque.enums.TipoLocalEstoque;

/**
 * DTO de retorno com os dados completos de um local de estoque.
 */
public record EstoqueLocalResponse(
        Long id,
        String nome,
        String descricao,
        TipoLocalEstoque tipo,
        Long empresaId
) {}
