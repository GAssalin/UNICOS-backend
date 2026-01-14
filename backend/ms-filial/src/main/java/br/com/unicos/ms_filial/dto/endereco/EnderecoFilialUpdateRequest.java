package br.com.unicos.ms_filial.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de atualização de EnderecoFilial.
 */
public record EnderecoFilialUpdateRequest(
        @NotNull Long filialId,
        @NotBlank String logradouro,
        @NotBlank String numero,
        String complemento,
        @NotBlank String bairro,
        @NotBlank String cidade,
        @NotBlank String uf,
        @NotBlank String cep
) { }
