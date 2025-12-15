package br.com.unicos.core.tesouraria.enums;

import lombok.Getter;

/**
 * Enum que define o tipo de lançamento financeiro realizado no sistema.
 * <p>
 * Indica a natureza da movimentação de valores em caixa ou conta bancária.
 */
@Getter
public enum TipoLancamentoFinanceiro {

    /**
     * Entrada de valores, como recebimentos e depósitos.
     */
    ENTRADA("Entrada"),

    /**
     * Saída de valores, como pagamentos e retiradas.
     */
    SAIDA("Saída"),

    /**
     * Movimentação interna entre contas da empresa.
     */
    TRANSFERENCIA("Transferência");

    private final String descricao;

    TipoLancamentoFinanceiro(String descricao) {
        this.descricao = descricao;
    }
}
