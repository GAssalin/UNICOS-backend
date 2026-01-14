package br.com.unicos.ms_filial.dto.parametro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de atualização de FilialParametro.
 */
public record FilialParametroUpdateRequest(
        @NotNull Long filialId,
        @NotBlank String chave,
        @NotBlank String valor,
        String descricao,
        @NotNull Boolean ativo
) { }
