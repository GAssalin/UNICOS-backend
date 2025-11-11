package br.com.unicos.core.tesouraria.enums;

import lombok.Getter;

/**
 * Enum que define os meios de pagamento aceitos em lançamentos financeiros.
 * <p>
 * Representa as formas de movimentação de valores disponíveis na tesouraria.
 */
@Getter
public enum MeioPagamento {

    /**
     * Pagamento realizado em dinheiro.
     */
    DINHEIRO("Dinheiro"),

    /**
     * Pagamento via transferência bancária.
     */
    TRANSFERENCIA("Transferência"),

    /**
     * Pagamento via PIX.
     */
    PIX("PIX"),

    /**
     * Pagamento com cheque.
     */
    CHEQUE("Cheque"),

    /**
     * Pagamento com cartão de crédito.
     */
    CARTAO_CREDITO("Cartão de crédito"),

    /**
     * Pagamento com cartão de débito.
     */
    CARTAO_DEBITO("Cartão de débito");

    private final String descricao;

    MeioPagamento(String descricao) {
        this.descricao = descricao;
    }
}
