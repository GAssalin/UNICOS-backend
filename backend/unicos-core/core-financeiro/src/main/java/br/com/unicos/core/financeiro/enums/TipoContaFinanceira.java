package br.com.unicos.core.financeiro.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de contas financeiras utilizadas pela empresa.
 * <p>
 * Indica a natureza da conta e sua finalidade dentro do controle financeiro.
 */
@Getter
public enum TipoContaFinanceira {

    /**
     * Conta bancária de uso corrente para movimentações diárias.
     */
    CONTA_CORRENTE("Conta Corrente"),

    /**
     * Conta bancária destinada à poupança.
     */
    CONTA_POUPANCA("Conta Poupança"),

    /**
     * Conta de caixa físico utilizada para recebimentos e pagamentos em espécie.
     */
    CONTA_CAIXA("Caixa Físico"),

    /**
     * Conta utilizada para aplicações ou investimentos financeiros.
     */
    CONTA_INVESTIMENTO("Conta de Investimento"),

    /**
     * Conta digital ou carteira virtual utilizada em plataformas eletrônicas.
     */
    CONTA_VIRTUAL("Conta Virtual");

    private final String descricao;

    TipoContaFinanceira(String descricao) {
        this.descricao = descricao;
    }

}
