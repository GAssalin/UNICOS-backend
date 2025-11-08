package br.com.unicos.ms_pagamento.dto.client;

/**
 * DTO de resposta que representa uma Forma de Pagamento
 * recebida do microserviço core-financeiro.
 */
public record FormaPagamentoResponse(
        Long id,
        String descricao,
        String tipo,
        boolean ativo
) {}
