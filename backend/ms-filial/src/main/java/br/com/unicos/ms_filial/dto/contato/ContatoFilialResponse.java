package br.com.unicos.ms_filial.dto.contato;

import jakarta.validation.constraints.Email;

/**
 * DTO de resposta de ContatoFilial.
 */
public record ContatoFilialResponse(
        Long id,
        Long filialId,
        String telefonePrincipal,
        String telefoneSecundario,
        String emailPrincipal,
        String emailSecundario,
        String nomeResponsavel
) { }
