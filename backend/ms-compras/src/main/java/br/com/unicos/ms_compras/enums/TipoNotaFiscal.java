package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de notas fiscais associadas ao módulo de compras.
 */
@Getter
public enum TipoNotaFiscal {

    /**
     * Nota fiscal de entrada de mercadorias.
     */
    ENTRADA("Nota fiscal de entrada de produtos adquiridos."),

    /**
     * Nota fiscal de devolução de compra.
     */
    DEVOLUCAO_COMPRA("Nota fiscal de devolução de produtos ao fornecedor."),

    /**
     * Nota fiscal complementar.
     */
    COMPLEMENTAR("Nota fiscal complementar para ajuste de valores."),

    /**
     * Nota de importação.
     */
    IMPORTACAO("Nota fiscal relativa à importação de mercadorias.");

    private final String descricao;

    TipoNotaFiscal(String descricao) {
        this.descricao = descricao;
    }
}
