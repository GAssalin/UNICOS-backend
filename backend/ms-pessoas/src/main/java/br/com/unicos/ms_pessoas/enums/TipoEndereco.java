package br.com.unicos.ms_pessoas.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de endereço associados a uma pessoa.
 */
@Getter
public enum TipoEndereco {

    /**
     * Endereço residencial (domicílio principal).
     */
    RESIDENCIAL("Residencial"),

    /**
     * Endereço comercial (local de trabalho ou sede da empresa).
     */
    COMERCIAL("Comercial"),

    /**
     * Endereço de cobrança (para correspondências financeiras).
     */
    COBRANCA("Cobrança"),

    /**
     * Endereço de entrega (para recebimento de mercadorias).
     */
    ENTREGA("Entrega");

    private final String descricao;

    TipoEndereco(String descricao) {
        this.descricao = descricao;
    }
}
