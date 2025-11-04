package br.com.unicos.ms_pessoas.dto;

import br.com.unicos.ms_pessoas.enums.TipoContato;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de criação e atualização de contatos de pessoa.
 */
public record ContatoPessoaRequest(
        @NotNull Long pessoaId,

        @NotNull TipoContato tipoContato,

        @NotBlank @Size(max = 100)
        String valor,

        @Size(max = 100)
        String descricao,

        boolean principal
) {}