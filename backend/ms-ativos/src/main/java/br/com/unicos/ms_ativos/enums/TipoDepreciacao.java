package br.com.unicos.ms_ativos.enums;

import lombok.Getter;

/**
 * Enum que define o tipo de depreciação aplicada ao ativo.
 */
@Getter
public enum TipoDepreciacao {

    /**
     * Depreciação linear, com valor fixo ao longo do tempo.
     */
    LINEAR("Depreciação linear"),

    /**
     * Depreciação acelerada, utilizada para ativos de uso intenso.
     */
    ACELERADA("Depreciação acelerada"),

    /**
     * Reavaliação positiva ou negativa do valor do ativo.
     */
    REAVALIACAO("Reavaliação contábil do ativo"),

    /**
     * Depreciação por horas de uso ou produção.
     */
    POR_USO("Depreciação proporcional ao uso ou horas trabalhadas");

    private final String descricao;

    TipoDepreciacao(String descricao) {
        this.descricao = descricao;
    }
}
