package br.com.erp.ms_produtos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO usado para listagem simplificada de histórico de preço.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPrecoListDTO {

    private Long id;
    private BigDecimal precoAnterior;
    private BigDecimal novoPreco;
    private LocalDateTime dataAlteracao;
}