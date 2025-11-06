package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de cotação possíveis.
 */
@Getter
public enum TipoCotacao {

    /**
     * Cotação feita manualmente dentro do sistema.
     */
    INTERNA("Cotação criada manualmente pelo comprador."),

    /**
     * Cotação enviada automaticamente para fornecedores via integração.
     */
    AUTOMATICA("Cotação gerada e distribuída automaticamente."),

    /**
     * Cotação de urgência para aquisição imediata.
     */
    EMERGENCIAL("Cotação emergencial para suprimento rápido.");

    private final String descricao;

    TipoCotacao(String descricao) {
        this.descricao = descricao;
    }
}
