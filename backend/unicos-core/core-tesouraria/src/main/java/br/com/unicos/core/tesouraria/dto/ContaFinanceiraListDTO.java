package br.com.unicos.core.tesouraria.dto;

import br.com.unicos.core.tesouraria.enums.TipoContaFinanceira;

import java.math.BigDecimal;

/**
 * DTO utilizado para listagem resumida de contas financeiras.
 */
public record ContaFinanceiraListDTO(
        Long id,
        String nomeConta,
        TipoContaFinanceira tipoConta,
        BigDecimal saldoAtual
) { }
