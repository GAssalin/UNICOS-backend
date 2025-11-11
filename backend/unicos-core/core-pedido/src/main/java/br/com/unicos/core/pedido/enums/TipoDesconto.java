package br.com.unicos.core.pedido.enums;

import lombok.Getter;

/**
 * Enum que representa o tipo de desconto aplicado ao pedido.
 * <p>
 * Utilizado para determinar como o valor do desconto será calculado.
 */
@Getter
public enum TipoDesconto {

    /**
     * Desconto percentual aplicado sobre o valor total do pedido.
     */
    PERCENTUAL("Percentual"),

    /**
     * Desconto de valor fixo aplicado diretamente ao pedido.
     */
    FIXO("Fixo");

    private final String descricao;

    TipoDesconto(String descricao) {
        this.descricao = descricao;
    }

}
