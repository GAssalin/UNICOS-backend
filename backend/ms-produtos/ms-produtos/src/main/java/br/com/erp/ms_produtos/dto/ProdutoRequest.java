package br.com.erp.ms_produtos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank(message = "O nome do produto é obrigatório.")
        String nome,

        @Size(max = 500)
        String descricao,

        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
        BigDecimal preco,

        @NotNull(message = "A categoria é obrigatória.")
        Long categoriaId,

        Long marcaId,

        @NotBlank(message = "O SKU é obrigatório.")
        String sku,

        Boolean ativo
) {
}