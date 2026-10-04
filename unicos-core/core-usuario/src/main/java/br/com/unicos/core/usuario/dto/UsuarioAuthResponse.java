package br.com.unicos.core.usuario.dto;

/**
 * Dados do usuário necessários ao ms-autenticacao para validar credenciais e emitir tokens.
 *
 * <p>Trafega apenas entre serviços, por endpoints {@code /internal/**}.</p>
 */
public record UsuarioAuthResponse(
        Long userId,
        String login,
        String passwordHash,
        Long empresaId,
        boolean ativo
) {}
