package br.com.unicos.ms_produtos.dto.historico_preco;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO utilizado em listagens de histórico de preço,
 * trazendo apenas os dados essenciais.
 */
public record HistoricoPrecoListDTO(
        Long id,
        BigDecimal precoAnterior,
        BigDecimal novoPreco,
        LocalDateTime dataAlteracao
) {}
