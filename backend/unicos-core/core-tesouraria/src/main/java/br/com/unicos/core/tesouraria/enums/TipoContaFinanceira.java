package br.com.unicos.core.tesouraria.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de contas financeiras gerenciadas pela tesouraria.
 * <p>
 * Define a origem dos recursos financeiros da empresa.
 */
@Getter
public enum TipoContaFinanceira {

    /**
     * Conta bancária corrente.
     */
    CONTA_CORRENTE("Conta corrente"),

    /**
     * Conta bancária poupança.
     */
    CONTA_POUPANCA("Conta poupança"),

    /**
     * Caixa físico mantido em espécie.
     */
    CAIXA_FISICO("Caixa físico"),

    /**
     * Conta de investimento ou aplicação financeira.
     */
    CONTA_INVESTIMENTO("Conta de investimento");

    private final String descricao;

    TipoContaFinanceira(String descricao) {
        this.descricao = descricao;
    }
}
