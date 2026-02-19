package br.com.unicos.ms_estoque.dto.estoque;

import br.com.unicos.ms_estoque.enums.StatusEstoque;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para atualização de {@code Estoque}.
 * <p>
 * Mantém o mesmo shape do create para simplificar o MVP.
 *
 * @param codigo             Código interno do estoque (único no tenant).
 * @param nome               Nome do estoque.
 * @param descricao          Descrição livre (opcional).
 * @param statusEstoque Status do estoque.
 * @param estoquePaiId  Identificador lógico do estoque pai (opcional).
 */
public record EstoqueUpdateRequestDto(
        @NotBlank String codigo,
        @NotBlank String nome,
        String descricao,
        @NotNull StatusEstoque statusEstoque,
        Long estoquePaiId
) { }
