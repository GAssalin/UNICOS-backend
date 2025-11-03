package br.com.erp.ms_produtos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO usado para listagem simplificada de histórico de preço.
 */
public record HistoricoPrecoListDTO(
        Long id,
        BigDecimal precoAnterior,
        BigDecimal novoPreco,
        LocalDateTime dataAlteracao
) {}