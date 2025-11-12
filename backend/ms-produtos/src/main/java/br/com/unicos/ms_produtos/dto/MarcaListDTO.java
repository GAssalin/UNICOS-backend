package br.com.unicos.ms_produtos.dto;

/**
 * DTO usado para listagem simplificada de marcas.
 */
public record MarcaListDTO(
        Long id,
        String nome
) {}