package br.com.unicos.ms_produtos.dto.marca;

/**
 * DTO de retorno que representa uma marca cadastrada,
 * incluindo informações complementares e status.
 */
public record MarcaResponse(
        Long id,
        String nome,
        String descricao,
        String paisOrigem,
        Boolean ativo
) {}
