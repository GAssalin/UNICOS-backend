package br.com.unicos.ms_pessoas.usuario.dto.permissao;

/**
 * DTO com dados resumidos de uma role retornada pelo ms-permissao.
 */
public record RoleResumoResponse(
        Long id,
        String nome
) {}
