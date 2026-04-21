package br.com.unicos.ms_estoque.dto.estoque;

import br.com.unicos.ms_estoque.enums.StatusEstoque;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para atualização de {@code Estoque}.
 *
 * @param codigo código interno do estoque, único por empresa.
 * @param nome nome do estoque.
 * @param descricao descrição livre do estoque.
 * @param statusEstoque status do estoque.
 * @param estoquePaiId identificador lógico do estoque pai.
 */
public record EstoqueUpdateRequestDto(

        @NotBlank(message = "O código do estoque é obrigatório.")
        @Size(max = 30, message = "O código do estoque deve ter no máximo 30 caracteres.")
        String codigo,

        @NotBlank(message = "O nome do estoque é obrigatório.")
        @Size(max = 200, message = "O nome do estoque deve ter no máximo 200 caracteres.")
        String nome,

        @Size(max = 500, message = "A descrição do estoque deve ter no máximo 500 caracteres.")
        String descricao,

        @NotNull(message = "O status do estoque é obrigatório.")
        StatusEstoque statusEstoque,

        Long estoquePaiId
) { }