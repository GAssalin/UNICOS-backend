package br.com.unicos.core.financeiro.enums;

import lombok.Getter;

/**
 * Enum que representa a periodicidade de lançamentos financeiros recorrentes.
 * <p>
 * Utilizado para identificar a frequência de repetição de cobranças, pagamentos ou receitas.
 */
@Getter
public enum Periodicidade {

    /**
     * Lançamento único, sem recorrência.
     */
    UNICO("Lançamento Único"),

    /**
     * Lançamento recorrente mensal.
     */
    MENSAL("Mensal"),

    /**
     * Lançamento recorrente a cada dois meses.
     */
    BIMESTRAL("Bimestral"),

    /**
     * Lançamento recorrente a cada três meses.
     */
    TRIMESTRAL("Trimestral"),

    /**
     * Lançamento recorrente anual.
     */
    ANUAL("Anual");

    private final String descricao;

    Periodicidade(String descricao) {
        this.descricao = descricao;
    }

}
