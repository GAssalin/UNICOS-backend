package br.com.unicos.core.financeiro.enums;

import lombok.Getter;

/**
 * Enum que representa o status de um lançamento ou título financeiro.
 * <p>
 * Indica a situação atual do pagamento, recebimento ou processamento financeiro.
 */
@Getter
public enum StatusFinanceiro {

    /**
     * Lançamento pendente de pagamento ou recebimento.
     */
    PENDENTE("Pendente"),

    /**
     * Lançamento quitado totalmente.
     */
    PAGO("Pago"),

    /**
     * Lançamento cancelado por alguma ocorrência.
     */
    CANCELADO("Cancelado"),

    /**
     * Lançamento com data de vencimento expirada e ainda não quitado.
     */
    ATRASADO("Atrasado"),

    /**
     * Lançamento parcialmente quitado.
     */
    PARCIALMENTE_PAGO("Parcialmente Pago");

    private final String descricao;

    StatusFinanceiro(String descricao) {
        this.descricao = descricao;
    }

}
