package br.com.erp.ms_pagamento.enums;

/**
 * Enum que representa o status de uma parcela de pagamento.
 *
 * Indica o andamento individual de cada parcela associada a um pagamento.
 */
public enum StatusParcela {

    /** Parcela pendente de pagamento. */
    PENDENTE("Pendente"),

    /** Parcela quitada com sucesso. */
    QUITADA("Quitada"),

    /** Parcela vencida e não quitada. */
    ATRASADA("Atrasada");

    private final String descricao;

    StatusParcela(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}