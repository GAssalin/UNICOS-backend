package br.com.unicos.ms_filial.dto.parametro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de criação de FilialParametro.
 */
public record FilialParametroCreateRequest(
        @NotNull Long filialId,
        @NotBlank String chave,
        @NotBlank String valor,
        String descricao,
        @NotNull Boolean ativo
) { }
