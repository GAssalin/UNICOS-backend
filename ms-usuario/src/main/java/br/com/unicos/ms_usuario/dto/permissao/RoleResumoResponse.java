package br.com.unicos.ms_usuario.dto.permissao;

/**
 * DTO com dados resumidos de uma role retornada pelo ms-permissao.
 */
public record RoleResumoResponse(
        Long id,
        String nome
) {}
