package br.com.erp.ms_produtos.dto;

/**
 * DTO usado para listagem simples de unidades de medida.
 */
public record UnidadeMedidaListDTO(
        Long id,
        String nome,
        String sigla
) {}