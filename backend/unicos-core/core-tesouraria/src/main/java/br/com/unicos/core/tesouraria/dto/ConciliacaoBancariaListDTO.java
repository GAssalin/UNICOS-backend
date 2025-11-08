package br.com.unicos.core.tesouraria.dto;

import br.com.unicos.core.tesouraria.enums.StatusConciliacao;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para listagem resumida de conciliações bancárias.
 */
public record ConciliacaoBancariaListDTO(
        Long id,
        String contaFinanceiraNome,
        StatusConciliacao status,
        BigDecimal valorConciliado,
        LocalDate dataConciliacao
) { }
