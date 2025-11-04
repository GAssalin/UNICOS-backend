package br.com.unicos.ms_pessoas.enums;

import lombok.Getter;

/**
 * Enum que representa o tipo de logradouro (rua, avenida, etc.).
 */
@Getter
public enum TipoLogradouro {

    /** Rua. */
    RUA("Rua"),

    /** Avenida. */
    AVENIDA("Avenida"),

    /** Travessa. */
    TRAVESSA("Travessa"),

    /** Alameda. */
    ALAMEDA("Alameda"),

    /** Rodovia. */
    RODOVIA("Rodovia"),

    /** Praça. */
    PRACA("Praça"),

    /** Outro tipo de logradouro. */
    OUTRO("Outro");

    private final String descricao;

    TipoLogradouro(String descricao) {
        this.descricao = descricao;
    }

}
