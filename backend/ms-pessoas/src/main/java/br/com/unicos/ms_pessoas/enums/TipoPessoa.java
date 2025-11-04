package br.com.unicos.ms_pessoas.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de pessoa no sistema.
 */
@Getter
public enum TipoPessoa {

    /**
     * Pessoa física (identificada por CPF).
     */
    FISICA("Pessoa Física"),

    /**
     * Pessoa jurídica (identificada por CNPJ).
     */
    JURIDICA("Pessoa Jurídica");

    private final String descricao;

    TipoPessoa(String descricao) {
        this.descricao = descricao;
    }
}
