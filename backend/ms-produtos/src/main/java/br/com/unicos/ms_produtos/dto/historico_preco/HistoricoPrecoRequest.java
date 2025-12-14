package br.com.unicos.ms_produtos.dto.historico_preco;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO utilizado para registrar uma nova alteração de preço de um produto.
 *
 * <p>
 * Contém o preço anterior, o novo preço e um motivo opcional.
 * A data da alteração é gerada automaticamente pelo sistema.
 * </p>
 */
public record HistoricoPrecoRequest(

        @NotNull(message = "O preço anterior é obrigatório.")
        @DecimalMin(value = "0.01", message = "O preço anterior deve ser maior que zero.")
        BigDecimal precoAnterior,

        @NotNull(message = "O novo preço é obrigatório.")
        @DecimalMin(value = "0.01", message = "O novo preço deve ser maior que zero.")
        BigDecimal novoPreco,

        @Size(max = 500, message = "O motivo deve ter no máximo 500 caracteres.")
        String motivo
) {}
