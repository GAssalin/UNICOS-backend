package br.com.unicos.ms_produtos.dto;

/**
 * DTO usado para listagem simplificada de atributos personalizados de categorias.
 */
public record AtributoPersonalizadoListDTO(
        Long id,
        String nome,
        String categoriaNome
) {}
