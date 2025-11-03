package br.com.unicos.ms_pagamento.dto;

import br.com.unicos.ms_pagamento.enums.StatusPagamento;
import br.com.unicos.ms_pagamento.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Record que representa os dados de retorno de um pagamento.
 */
public record PagamentoResponse(
        Long id,
        BigDecimal valor,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        StatusPagamento status,
        TipoTransacao tipoTransacao,
        String referenciaId,
        String observacao,
        FormaPagamentoResponse formaPagamento,
        List<ParcelaPagamentoResponse> parcelas,
        TransacaoFinanceiraResponse transacaoFinanceira,
        List<MovimentoCaixaResponse> movimentosCaixa
) { }