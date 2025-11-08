package br.com.unicos.core.financeiro.enums;

import lombok.Getter;

/**
 * Enum que representa as formas de pagamento aceitas nos lançamentos financeiros.
 * <p>
 * Utilizado para indicar o meio pelo qual um pagamento foi realizado ou recebido.
 */
@Getter
public enum FormaPagamento {

    /**
     * Pagamento realizado em dinheiro.
     */
    DINHEIRO("Dinheiro"),

    /**
     * Pagamento realizado via PIX.
     */
    PIX("PIX"),

    /**
     * Pagamento por transferência bancária.
     */
    TRANSFERENCIA("Transferência Bancária"),

    /**
     * Pagamento realizado com cartão de crédito.
     */
    CARTAO_CREDITO("Cartão de Crédito"),

    /**
     * Pagamento realizado com cartão de débito.
     */
    CARTAO_DEBITO("Cartão de Débito"),

    /**
     * Pagamento através de boleto bancário.
     */
    BOLETO("Boleto Bancário"),

    /**
     * Pagamento realizado por meio de cheque.
     */
    CHEQUE("Cheque");

    private final String descricao;

    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

}
