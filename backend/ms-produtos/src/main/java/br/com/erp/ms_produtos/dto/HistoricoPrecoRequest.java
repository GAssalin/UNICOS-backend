package br.com.erp.ms_produtos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO usado para criação de registros de histórico de preço.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPrecoRequest {

    @NotNull(message = "O ID do produto é obrigatório.")
    private Long produtoId;

    @NotNull(message = "O preço anterior é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço anterior deve ser maior que zero.")
    private BigDecimal precoAnterior;

    @NotNull(message = "O novo preço é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O novo preço deve ser maior que zero.")
    private BigDecimal novoPreco;

    private String motivo;
}