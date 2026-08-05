package br.com.unicos.ms_estoque.dto.estoque;

import br.com.unicos.ms_estoque.enums.StatusEstoque;

/**
 * DTO utilizado para retorno de dados de {@code Estoque}.
 *
 * @param id identificador do estoque.
 * @param codigo código interno do estoque.
 * @param nome nome do estoque.
 * @param descricao descrição livre do estoque.
 * @param statusEstoque status do estoque.
 * @param estoquePaiId identificador lógico do estoque pai.
 */
public record EstoqueResponseDto(
        Long id,
        String codigo,
        String nome,
        String descricao,
        StatusEstoque statusEstoque,
        Long estoquePaiId
) { }