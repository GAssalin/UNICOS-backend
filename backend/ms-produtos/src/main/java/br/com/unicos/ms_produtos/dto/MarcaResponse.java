package br.com.unicos.ms_produtos.dto;

/**
 * DTO usado para retorno detalhado de marca.
 */
public record MarcaResponse(
        Long id,
        String nome,
        Integer quantidadeProdutos
) {}