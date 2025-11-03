package br.com.unicos.ms_pagamento.dto;

import br.com.unicos.ms_pagamento.enums.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Record que representa os dados de entrada para criação ou atualização de uma conta financeira.
 */
public record ContaFinanceiraRequest(

        @NotBlank(message = "A descrição da conta é obrigatória.")
        String descricao,

        @Size(max = 50, message = "O nome do banco deve ter no máximo 50 caracteres.")
        String banco,

        @Size(max = 20, message = "A agência deve ter no máximo 20 caracteres.")
        String agencia,

        @Size(max = 30, message = "O número da conta deve ter no máximo 30 caracteres.")
        String numeroConta,

        @NotNull(message = "O tipo da conta é obrigatório.")
        TipoConta tipoConta,

        @NotNull(message = "O saldo atual é obrigatório.")
        BigDecimal saldoAtual
) { }