package br.com.unicos.ms_departamento.enums;

import lombok.Getter;

/**
 * Enum que representa o papel do responsável no departamento.
 * <p>
 * Define a função exercida pelo responsável, utilizada para fins de
 * governança, aprovação e organização interna.
 */
@Getter
public enum PapelResponsavelDepartamento {

    /**
     * Responsável principal pela gestão do departamento.
     */
    GESTOR("Gestor"),

    /**
     * Responsável substituto/apoio ao gestor do departamento.
     */
    SUBGESTOR("Subgestor"),

    /**
     * Responsável de referência para tratativas específicas do departamento.
     */
    PONTO_FOCAL("Ponto focal");

    private final String descricao;

    PapelResponsavelDepartamento(String descricao) {
        this.descricao = descricao;
    }
}
