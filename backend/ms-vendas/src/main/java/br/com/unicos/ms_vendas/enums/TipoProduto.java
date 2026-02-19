package br.com.unicos.ms_vendas.enums;

import lombok.Getter;

/**
 * Enumeração que representa os tipos padrão de produto no UniCoS.
 *
 * <p>
 * Pode ser utilizada para classificações básicas e rápidas.
 * Para cenários mais complexos e dinâmicos, recomenda-se utilizar
 * a entidade {@code ProdutoTipo}.
 * </p>
 */
@Getter
public enum TipoProduto {

    /**
     * Produto físico que pode possuir estoque.
     */
    PRODUTO("Produto físico"),

    /**
     * Serviço prestado, sem controle de estoque.
     */
    SERVICO("Serviço"),

    /**
     * Produto digital ou licença.
     */
    DIGITAL("Produto digital"),

    /**
     * Assinatura ou recorrência.
     */
    ASSINATURA("Assinatura");

    private final String descricao;

    TipoProduto(String descricao) {
        this.descricao = descricao;
    }

}
