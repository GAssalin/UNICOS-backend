package br.com.unicos.ms_permissao.dto.internal;

/**
 * DTO interno resumido para consumo por outros microserviços.
 */
public record RoleResumoResponse(
        Long id,
        String nome
) {}
