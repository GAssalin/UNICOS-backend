package br.com.unicos.ms_pessoas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação e atualização dos tipos de relação de pessoa.
 */
public record TipoRelacaoPessoaRequest(

        @NotBlank @Size(max = 30)
        String codigo,

        @NotBlank @Size(max = 100)
        String descricao,

        boolean ativo
) {}