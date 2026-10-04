package br.com.unicos.ms_funcionario.enums;

import lombok.Getter;

/**
 * Situação do vínculo do funcionário com a empresa.
 */
@Getter
public enum StatusFuncionario {

    /**
     * Funcionário em atividade.
     */
    ATIVO("Ativo"),

    /**
     * Funcionário em férias.
     */
    FERIAS("Férias"),

    /**
     * Funcionário afastado temporariamente.
     */
    AFASTADO("Afastado"),

    /**
     * Funcionário desligado da empresa.
     */
    DESLIGADO("Desligado");

    private final String descricao;

    StatusFuncionario(String descricao) {
        this.descricao = descricao;
    }
}
