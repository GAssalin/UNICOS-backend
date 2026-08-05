package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que representa o papel do responsável no estoque.
 * <p>
 * Define a função exercida pelo responsável, utilizada para fins de
 * governança, aprovação e organização interna.
 */
@Getter
public enum PapelResponsavelEstoque {

    /**
     * Responsável principal pela gestão do estoque.
     */
    GESTOR("Gestor"),

    /**
     * Responsável substituto/apoio ao gestor do estoque.
     */
    SUBGESTOR("Subgestor"),

    /**
     * Responsável de referência para tratativas específicas do estoque.
     */
    PONTO_FOCAL("Ponto focal");

    private final String descricao;

    PapelResponsavelEstoque(String descricao) {
        this.descricao = descricao;
    }
}
