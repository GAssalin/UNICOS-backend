package br.com.unicos.ms_produtos.dto;

/**
 * DTO usado para listagem simplificada de atributos personalizados de produtos.
 */
public record AtributoPersonalizadoListDTO(
        Long id,
        String nome,
        String valor,
        String produtoNome
) {}