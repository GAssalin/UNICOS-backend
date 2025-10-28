package br.com.erp.ms_produtos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO usado para criação e atualização de FornecedorProduto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorProdutoRequest {

    @NotNull(message = "O ID do fornecedor é obrigatório.")
    private Long fornecedorId;

    @NotNull(message = "O ID do produto é obrigatório.")
    private Long produtoId;

    @NotNull(message = "O preço de custo é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço de custo deve ser maior que zero.")
    private BigDecimal precoCusto;

    @PositiveOrZero(message = "O prazo de entrega deve ser zero ou positivo.")
    private Integer prazoEntregaDias;
}