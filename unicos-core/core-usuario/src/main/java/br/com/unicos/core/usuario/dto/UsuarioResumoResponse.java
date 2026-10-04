package br.com.unicos.core.usuario.dto;

/**
 * Identificação resumida de um usuário para exibição em outros serviços.
 *
 * @param id   identificador do usuário
 * @param nome nome da pessoa vinculada ao usuário ou, na ausência dela, o login
 */
public record UsuarioResumoResponse(
        Long id,
        String nome
) {}
