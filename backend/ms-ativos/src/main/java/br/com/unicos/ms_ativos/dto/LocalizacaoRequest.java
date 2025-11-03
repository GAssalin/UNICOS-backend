package br.com.unicos.ms_ativos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para criação e atualização de localizações.
 */
public record LocalizacaoRequest(
        @NotBlank @Size(max = 100)
        String descricao,

        @Size(max = 10)
        String andar,

        @Size(max = 20)
        String bloco,

        @NotNull
        Long filialId
) {}