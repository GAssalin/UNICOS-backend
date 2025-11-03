package br.com.erp.ms_produtos.dto;

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