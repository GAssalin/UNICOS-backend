package br.com.unicos.ms_produtos.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação ou atualização de categorias.
 *
 * <p>
 * Permite definir nome, descrição e categoria pai no modelo hierárquico.
 * </p>
 */
public record CategoriaRequest(
        @NotBlank(message = "O nome da categoria é obrigatório.")
        @Size(max = 100)
        String nome,
        @Size(max = 255)
        String descricao,
        Long categoriaPaiId
) {}
