package br.com.unicos.core.usuario.dto;

import java.util.Set;

public record UsuarioAuthResponse(
        Long userId,
        String login,
        String passwordHash,
        Long empresaId
) {}
