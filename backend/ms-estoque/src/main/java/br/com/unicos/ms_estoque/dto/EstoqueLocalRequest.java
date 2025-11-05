package br.com.unicos.ms_estoque.dto;

import br.com.unicos.ms_estoque.enums.TipoLocalEstoque;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para criação e atualização de locais de estoque.
 */
public record EstoqueLocalRequest(
        @NotBlank String nome,
        String descricao,
        @NotNull TipoLocalEstoque tipo,
        @NotNull Long empresaId
) {}
