package br.com.erp.ms_pagamento.dto;

import br.com.erp.ms_pagamento.enums.TipoFormaPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Record que representa os dados de entrada para criação ou atualização de uma forma de pagamento.
 */
public record FormaPagamentoRequest(

        @NotBlank(message = "A descrição da forma de pagamento é obrigatória.")
        String descricao,

        @NotNull(message = "O tipo de forma de pagamento é obrigatório.")
        TipoFormaPagamento tipo,

        @NotNull(message = "O campo 'ativo' é obrigatório.")
        Boolean ativo
) { }