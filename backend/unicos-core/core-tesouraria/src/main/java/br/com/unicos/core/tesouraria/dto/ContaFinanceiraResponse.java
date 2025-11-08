package br.com.unicos.core.tesouraria.dto;

import br.com.unicos.core.tesouraria.enums.MeioPagamento;
import br.com.unicos.core.tesouraria.enums.TipoContaFinanceira;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta para exibição dos dados completos de uma conta financeira.
 */
public record ContaFinanceiraResponse(
        Long id,
        String nomeConta,
        TipoContaFinanceira tipoConta,
        String banco,
        String agencia,
        String numeroConta,
        MeioPagamento meioPagamentoPadrao,
        BigDecimal saldoAtual,
        LocalDate dataAtualizacaoSaldo,
        Long empresaId,
        Long filialId
) { }
