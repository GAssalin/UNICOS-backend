package br.com.unicos.core.tesouraria.dto;

import br.com.unicos.core.tesouraria.enums.MeioPagamento;
import br.com.unicos.core.tesouraria.enums.TipoContaFinanceira;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para criação ou atualização de uma conta financeira.
 */
public record ContaFinanceiraRequest(
        @NotBlank String nomeConta,
        @NotNull TipoContaFinanceira tipoConta,
        String banco,
        String agencia,
        String numeroConta,
        MeioPagamento meioPagamentoPadrao,
        @NotNull BigDecimal saldoAtual,
        @NotNull LocalDate dataAtualizacaoSaldo,
        @NotNull Long empresaId,
        Long filialId
) { }
