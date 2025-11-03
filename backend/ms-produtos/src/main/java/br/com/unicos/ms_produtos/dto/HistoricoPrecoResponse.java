package br.com.unicos.ms_produtos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO usado para retorno detalhado de histórico de preço.
 */
public record HistoricoPrecoResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        BigDecimal precoAnterior,
        BigDecimal novoPreco,
        LocalDateTime dataAlteracao,
        String motivo
) {}