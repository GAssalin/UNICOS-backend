package br.com.unicos.core.auth.dto;

import java.util.Set;

public record TokenValidationResponse(
        Long usuarioId,
        Long empresaId,
        Set<String> roles
) {}