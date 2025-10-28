package br.com.erp.ms_produtos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para criação e atualização de vínculos entre produto e unidade de medida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoUnidadeRequest {

    @NotNull(message = "O ID do produto é obrigatório.")
    private Long produtoId;

    @NotNull(message = "O ID da unidade de medida é obrigatório.")
    private Long unidadeMedidaId;

    @NotNull(message = "A quantidade padrão é obrigatória.")
    @Positive(message = "A quantidade deve ser maior que zero.")
    private Double quantidadePadrao;
}