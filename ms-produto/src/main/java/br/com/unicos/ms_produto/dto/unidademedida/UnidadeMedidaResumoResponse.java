package br.com.unicos.ms_produto.dto.unidademedida;

public record UnidadeMedidaResumoResponse(
        Long id,
        String codigo,
        String descricao,
        Boolean fracionavel,
        Boolean ativo
) {}
