package br.com.erp.ms_produtos.dto;

/**
 * DTO usado para retorno detalhado de unidade de medida.
 */
public record UnidadeMedidaResponse(
        Long id,
        String nome,
        String sigla
) {}