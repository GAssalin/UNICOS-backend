package br.com.unicos.ms_pessoas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação e atualização de cargos.
 */
public record CargoRequest(

        @NotBlank @Size(max = 100)
        String nome,

        @Size(max = 255)
        String descricao,

        boolean ativo
) {}