package br.com.unicos.ms_produtos.dto.unidade_medida;

/**
 * DTO de retorno que representa uma unidade de medida cadastrada,
 * incluindo informações completas e status operacional.
 */
public record UnidadeMedidaResponse(
        Long id,
        String nome,
        String sigla,
        String descricao,
        Boolean ativo
) {}
