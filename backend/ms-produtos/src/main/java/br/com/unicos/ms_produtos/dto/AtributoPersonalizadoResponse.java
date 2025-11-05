package br.com.unicos.ms_produtos.dto;

/**
 * DTO usado para retorno detalhado de atributos personalizados de categorias.
 */
public record AtributoPersonalizadoResponse(
        Long id,
        Long categoriaId,
        String categoriaNome,
        String nome
) {}
