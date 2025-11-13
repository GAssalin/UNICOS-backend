package br.com.unicos.ms_produtos.dto.historico_preco;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de retorno que representa um registro completo de alteração de preço,
 * incluindo preços, data da mudança e motivo.
 */
public record HistoricoPrecoResponse(
        Long id,
        Long produtoId,
        BigDecimal precoAnterior,
        BigDecimal novoPreco,
        LocalDateTime dataAlteracao,
        String motivo
) {}
