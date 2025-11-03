package br.com.unicos.ms_produtos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO usado para criação e atualização de vínculos entre produto e unidade de medida.
 */
public record ProdutoUnidadeRequest(

        @NotNull(message = "O ID do produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "O ID da unidade de medida é obrigatório.")
        Long unidadeMedidaId,

        @NotNull(message = "A quantidade padrão é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        Double quantidadePadrao
) {}