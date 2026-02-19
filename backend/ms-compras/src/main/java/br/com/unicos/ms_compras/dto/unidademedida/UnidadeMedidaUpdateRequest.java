package br.com.unicos.ms_compras.dto.unidademedida;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UnidadeMedidaUpdateRequest(
        @NotBlank String codigo,
        @NotBlank String descricao,
        @NotNull Boolean fracionavel
) {}
