package br.com.unicos.core_produto.enums;

import lombok.Getter;

/**
 * Enum que define o tipo de controle de estoque aplicado ao produto.
 *
 * <p>
 * Determina o método de rastreabilidade e contabilização de entradas e saídas
 * nos módulos de estoque e compras.
 * </p>
 */
@Getter
public enum TipoControleEstoque {

    /**
     * Controle simples, apenas por quantidade total.
     */
    QUANTITATIVO("Controle por quantidade"),

    /**
     * Controle por lote (com data de validade ou fabricação).
     */
    LOTE("Controle por lote"),

    /**
     * Controle individual por número de série.
     */
    SERIE("Controle por número de série");

    private final String descricao;

    TipoControleEstoque(String descricao) {
        this.descricao = descricao;
    }
}
