package br.com.unicos.ms_produtos.dto.produto_atributo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criar ou atualizar o valor de um atributo
 * personalizado associado a um produto.
 *
 * <p>
 * Representa a combinação "atributo + valor" (ex.: "Cor = Azul",
 * "Tamanho = G").
 * </p>
 */
public record ProdutoAtributoValorRequest(
        @NotNull(message = "O ID do atributo é obrigatório.")
        Long atributoPersonalizadoId,
        @NotBlank(message = "O valor do atributo é obrigatório.")
        @Size(max = 100)
        String valor
) {}
