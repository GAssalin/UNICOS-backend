package br.com.unicos.ms_pessoas.dto.contato;

import br.com.unicos.ms_pessoas.enums.TipoContato;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de contatos de uma pessoa.
 * <p>
 * Permite cadastrar telefone, celular ou e-mail, além de definir se é o contato principal.
 * </p>
 */
public record ContatoRequest(

        @NotNull(message = "O ID da pessoa é obrigatório.")
        Long pessoaId,

        @NotNull(message = "O tipo do contato é obrigatório.")
        TipoContato tipo,

        @NotBlank(message = "O valor do contato é obrigatório.")
        @Size(max = 150, message = "O valor do contato deve ter no máximo 150 caracteres.")
        String valor,

        Boolean principal
) { }
