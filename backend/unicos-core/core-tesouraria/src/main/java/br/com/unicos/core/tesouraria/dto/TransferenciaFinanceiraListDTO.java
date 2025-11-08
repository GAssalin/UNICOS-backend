package br.com.unicos.core.tesouraria.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para listagem resumida de transferências financeiras.
 */
public record TransferenciaFinanceiraListDTO(
        Long id,
        String contaOrigemNome,
        String contaDestinoNome,
        BigDecimal valor,
        LocalDate dataTransferencia
) { }
