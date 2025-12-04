package br.com.unicos.ms_produtos.dto.historico_preco;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO utilizado para registrar uma nova alteração de preço de um produto.
 *
 * <p>
 * Contém o preço anterior, o novo preço e um motivo opcional.
 * A data da alteração será preenchida automaticamente pelo sistema.
 * </p>
 */
public record HistoricoPrecoRequest(
        @NotNull
        @Positive(message = "O preço anterior deve ser maior que zero.")
        BigDecimal precoAnterior,
        @NotNull
        @Positive(message = "O novo preço deve ser maior que zero.")
        BigDecimal novoPreco,
        @Size(max = 500)
        String motivo
) {}
