package br.com.unicos.core.financeiro.enums;

import lombok.Getter;

/**
 * Enum que representa a natureza financeira de um lançamento.
 * <p>
 * Indica a classificação do gasto ou receita conforme sua finalidade dentro da empresa.
 */
@Getter
public enum NaturezaFinanceira {

    /**
     * Movimentação relacionada à operação direta da empresa.
     */
    OPERACIONAL("Operacional"),

    /**
     * Movimentação vinculada à administração e suporte da empresa.
     */
    ADMINISTRATIVA("Administrativa"),

    /**
     * Movimentação relacionada a investimentos e aquisições de ativos.
     */
    INVESTIMENTO("Investimento"),

    /**
     * Movimentação que não se enquadra nas categorias anteriores.
     */
    OUTROS("Outros");

    private final String descricao;

    NaturezaFinanceira(String descricao) {
        this.descricao = descricao;
    }

}
