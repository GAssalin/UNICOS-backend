package br.com.unicos.ms_auth.dto.usuario;

import java.util.Set;

/**
 * DTO utilizado para criação ou atualização de usuários.
 * <p>
 * Contém apenas os dados necessários para operações de escrita,
 * não expondo informações sensíveis ou geradas automaticamente.
 */
public record UsuarioRequest(
        String login,
        Long pessoaId,
        String password,
        String email,
        Boolean ativo,
        Set<Long> rolesIds
) {}
