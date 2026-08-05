package br.com.unicos.core.auth.dto;

public record TokenValidationResponse(
        Long usuarioId,
        Long empresaId
) {}