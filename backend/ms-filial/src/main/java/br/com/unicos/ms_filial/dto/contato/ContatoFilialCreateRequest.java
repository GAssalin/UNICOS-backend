package br.com.unicos.ms_filial.dto.contato;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de criação de ContatoFilial.
 */
public record ContatoFilialCreateRequest(
        @NotNull Long filialId,
        @NotBlank String telefonePrincipal,
        String telefoneSecundario,
        @Email @NotBlank String emailPrincipal,
        @Email String emailSecundario,
        String nomeResponsavel
) { }
