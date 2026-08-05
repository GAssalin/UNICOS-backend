package br.com.unicos.ms_usuario.dto.usuario;

/**
 * DTO resumido para listagem de usuários.
 */
public record UsuarioListDTO(
        Long id,
        String login,
        String email,
        boolean emailVerificado,
        boolean ativo,
        Long roleId,
        String roleNome
) {}
