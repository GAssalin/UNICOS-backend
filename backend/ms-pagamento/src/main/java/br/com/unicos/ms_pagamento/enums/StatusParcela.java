package br.com.unicos.ms_pagamento.enums;

import lombok.Getter;

/**
 * Enum que representa o status de uma parcela de pagamento.
 * <p>
 * Indica o andamento individual de cada parcela associada a um pagamento.
 */
@Getter
public enum StatusParcela {

    /**
     * Parcela pendente de pagamento.
     */
    PENDENTE("Pendente"),

    /**
     * Parcela quitada com sucesso.
     */
    QUITADA("Quitada"),

    /**
     * Parcela vencida e não quitada.
     */
    ATRASADA("Atrasada");

    private final String descricao;

    StatusParcela(String descricao) {
        this.descricao = descricao;
    }
}