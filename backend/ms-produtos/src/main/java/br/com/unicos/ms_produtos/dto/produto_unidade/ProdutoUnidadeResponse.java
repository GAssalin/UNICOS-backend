package br.com.unicos.ms_produtos.dto.produto_unidade;

/**
 * DTO usado para retorno detalhado do vínculo entre produto e unidade de medida.
 */
public record ProdutoUnidadeResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        Long unidadeMedidaId,
        String unidadeMedidaNome,
        Double quantidadePadrao
) {}