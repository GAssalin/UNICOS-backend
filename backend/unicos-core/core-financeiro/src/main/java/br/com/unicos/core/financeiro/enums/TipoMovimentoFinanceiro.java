package br.com.unicos.core.financeiro.enums;

import lombok.Getter;

/**
 * Enum que representa o tipo de movimento financeiro.
 * <p>
 * Indica se o lançamento corresponde a um crédito ou débito no fluxo de caixa.
 */
@Getter
public enum TipoMovimentoFinanceiro {

    /**
     * Movimento de entrada de valores.
     */
    CREDITO("Crédito"),

    /**
     * Movimento de saída de valores.
     */
    DEBITO("Débito");

    private final String descricao;

    TipoMovimentoFinanceiro(String descricao) {
        this.descricao = descricao;
    }

}
