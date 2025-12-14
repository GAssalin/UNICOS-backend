package br.com.unicos.ms_produtos.dto.historico_preco;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO utilizado em listagens de histórico de preços,
 * contendo apenas informações essenciais para exibição.
 */
public record HistoricoPrecoListDTO(
        Long id,
        BigDecimal precoAnterior,
        BigDecimal novoPreco,
        LocalDateTime dataAlteracao
) {}
