package br.com.unicos.ms_pessoas.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de pessoa cadastrados no UniCoS.
 * <p>
 * Utilizado para diferenciar as regras de identificação e documentação
 * aplicáveis a pessoas físicas e jurídicas, garantindo integridade e
 * padronização no processamento dos dados.
 */
@Getter
public enum TipoPessoa {

    /**
     * Representa uma pessoa física identificada por CPF.
     */
    FISICA("Pessoa Física"),

    /**
     * Representa uma pessoa jurídica identificada por CNPJ.
     */
    JURIDICA("Pessoa Jurídica");

    private final String descricao;

    TipoPessoa(String descricao) {
        this.descricao = descricao;
    }
}
