package br.com.unicos.ms_empresa.enums;

import lombok.Getter;

/**
 * Enum que representa o regime tributário adotado pela empresa.
 * <p>
 * Utilizado principalmente por módulos fiscais, financeiros
 * e de obrigações legais.
 */
@Getter
public enum RegimeTributario {

    /**
     * Regime do Simples Nacional.
     */
    SIMPLES_NACIONAL("Simples Nacional"),

    /**
     * Regime de Lucro Presumido.
     */
    LUCRO_PRESUMIDO("Lucro Presumido"),

    /**
     * Regime de Lucro Real.
     */
    LUCRO_REAL("Lucro Real");

    private final String descricao;

    RegimeTributario(String descricao) {
        this.descricao = descricao;
    }
}
