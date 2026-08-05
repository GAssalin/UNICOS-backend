package br.com.unicos.ms_estoque.dto.estoque;

import br.com.unicos.ms_estoque.enums.StatusEstoque;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para filtros de pesquisa de {@code Estoque}.
 *
 * @param codigo código interno do estoque.
 * @param nome nome do estoque.
 * @param statusEstoque status do estoque.
 * @param estoquePaiId identificador lógico do estoque pai.
 * @param apenasRaiz quando true, retorna apenas estoques sem pai.
 */
public record EstoqueSearchRequestDto(

        @Size(max = 30, message = "O código do estoque deve ter no máximo 30 caracteres.")
        String codigo,

        @Size(max = 200, message = "O nome do estoque deve ter no máximo 200 caracteres.")
        String nome,

        StatusEstoque statusEstoque,
        Long estoquePaiId,
        Boolean apenasRaiz
) { }