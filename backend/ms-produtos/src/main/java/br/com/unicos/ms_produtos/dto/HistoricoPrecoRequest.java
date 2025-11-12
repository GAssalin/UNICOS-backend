package br.com.unicos.ms_produtos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * DTO usado para criação de registros de histórico de preço.
 */
public record HistoricoPrecoRequest(

        @NotNull(message = "O ID do produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "O preço anterior é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço anterior deve ser maior que zero.")
        BigDecimal precoAnterior,

        @NotNull(message = "O novo preço é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O novo preço deve ser maior que zero.")
        BigDecimal novoPreco,

        String motivo
) {}