package br.com.unicos.ms_produtos.dto.unidade_medida;

/**
 * DTO de retorno que representa uma unidade de medida cadastrada.
 *
 * <p>
 * Inclui identificador, informações completas da unidade
 * e status operacional.
 * </p>
 */
public record UnidadeMedidaResponse(
        Long id,
        String nome,
        String sigla,
        String descricao,
        Boolean ativo
) {}
