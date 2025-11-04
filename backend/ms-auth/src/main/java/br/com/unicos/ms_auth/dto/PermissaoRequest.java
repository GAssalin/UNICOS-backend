package br.com.unicos.ms_auth.dto;

/**
 * DTO para criação e atualização de permissões granulares.
 */
public record PermissaoRequest(
        String nome,
        String descricao
) {}