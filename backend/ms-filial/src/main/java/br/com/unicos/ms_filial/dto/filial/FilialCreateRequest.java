package br.com.unicos.ms_filial.dto.filial;

import br.com.unicos.ms_filial.enums.StatusFilial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de criação de Filial.
 */
public record FilialCreateRequest(
        @NotBlank String codigo,
        @NotBlank String nome,
        @NotBlank String cnpj,
        @NotNull StatusFilial statusFilial,
        @NotNull Long empresaId,
        Long enderecoFilialId,
        Long contatoFilialId
) { }
