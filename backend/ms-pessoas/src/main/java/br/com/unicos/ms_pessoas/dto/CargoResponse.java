package br.com.unicos.ms_pessoas.dto;

/**
 * DTO de retorno para cargos.
 */
public record CargoResponse(
        Long id,
        String nome,
        String descricao,
        boolean ativo
) {}