package br.com.unicos.core.produto.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de variações possíveis para produtos
 * que possuem combinações, como em e-commerces ou catálogos com SKUs múltiplos.
 *
 * <p>
 * Auxilia no cadastro de atributos usados para gerar combinações de estoque.
 * </p>
 */
@Getter
public enum TipoVariacaoProduto {

    /**
     * Variações relacionadas ao tamanho do produto.
     */
    TAMANHO("Tamanho"),

    /**
     * Variações relacionadas à cor do produto.
     */
    COR("Cor"),

    /**
     * Variações relacionadas ao material do produto.
     */
    MATERIAL("Material");

    private final String descricao;

    TipoVariacaoProduto(String descricao) {
        this.descricao = descricao;
    }
}
