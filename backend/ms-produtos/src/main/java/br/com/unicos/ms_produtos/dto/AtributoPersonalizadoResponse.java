package br.com.unicos.ms_produtos.dto;

/**
 * DTO usado para retorno detalhado de atributos personalizados.
 */
public record AtributoPersonalizadoResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        String nome,
        String valor
) {}