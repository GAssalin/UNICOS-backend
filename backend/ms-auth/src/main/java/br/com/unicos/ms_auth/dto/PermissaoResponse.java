package br.com.unicos.ms_auth.dto;

/**
 * DTO de resposta com informações de uma permissão.
 */
public record PermissaoResponse(
        Long id,
        String nome,
        String descricao
) {}