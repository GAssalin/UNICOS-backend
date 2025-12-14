package br.com.unicos.ms_produtos.dto.unidade_medida;

/**
 * DTO utilizado em listagens de unidades de medida,
 * retornando apenas informações essenciais para exibição.
 */
public record UnidadeMedidaListDTO(
        Long id,
        String nome,
        String sigla,
        Boolean ativo
) {}
