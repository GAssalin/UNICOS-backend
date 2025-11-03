package br.com.erp.ms_produtos.dto;

/**
 * DTO usado para listagem simples de categorias.
 */
public record CategoriaListDTO(
        Long id,
        String nome
) {}