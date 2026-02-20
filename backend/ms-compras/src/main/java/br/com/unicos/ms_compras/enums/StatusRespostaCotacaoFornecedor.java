package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Status da resposta do fornecedor para uma cotação.
 */
@Getter
public enum StatusRespostaCotacaoFornecedor {

    /**
     * Fornecedor ainda não respondeu.
     */
    PENDENTE("Pendente"),

    /**
     * Proposta enviada pelo fornecedor.
     */
    ENVIADA("Enviada"),

    /**
     * Fornecedor recusou participar.
     */
    RECUSADA("Recusada"),

    /**
     * Prazo expirado sem resposta.
     */
    VENCIDA("Vencida");

    private final String descricao;

    StatusRespostaCotacaoFornecedor(String descricao) {
        this.descricao = descricao;
    }
}
