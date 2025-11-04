package br.com.unicos.ms_pessoas.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de contato que uma pessoa pode possuir.
 */
@Getter
public enum TipoContato {

    /**
     * Telefone fixo.
     */
    TELEFONE("Telefone"),

    /**
     * Telefone celular.
     */
    CELULAR("Celular"),

    /**
     * Endereço de e-mail.
     */
    EMAIL("E-mail"),

    /**
     * Contato via WhatsApp.
     */
    WHATSAPP("WhatsApp"),

    /**
     * Outro tipo de contato.
     */
    OUTRO("Outro");

    private final String descricao;

    TipoContato(String descricao) {
        this.descricao = descricao;
    }

}
