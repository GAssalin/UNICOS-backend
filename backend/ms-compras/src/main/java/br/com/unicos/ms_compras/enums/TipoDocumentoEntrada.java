package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Tipos de documentos vinculados ao recebimento.
 */
@Getter
public enum TipoDocumentoEntrada {

    /**
     * Nota fiscal manual.
     */
    NF("Nota Fiscal"),

    /**
     * Nota fiscal eletrônica.
     */
    NFE("Nota Fiscal Eletrônica"),

    /**
     * Conhecimento de transporte eletrônico.
     */
    CTE("Conhecimento de Transporte"),

    /**
     * Recibo simples.
     */
    RECIBO("Recibo"),

    /**
     * Outros documentos.
     */
    OUTROS("Outros");

    private final String descricao;

    TipoDocumentoEntrada(String descricao) {
        this.descricao = descricao;
    }
}
