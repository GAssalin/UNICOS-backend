package br.com.unicos.core_produto.enums;

import lombok.Getter;

/**
 * Enum que representa a origem de fabricação ou aquisição do produto.
 *
 * <p>
 * Utilizada para controle fiscal e logístico, indicando se o produto
 * é nacional, importado diretamente ou adquirido no mercado interno.
 * </p>
 */
@Getter
public enum TipoOrigemProduto {

    /**
     * Produto de origem nacional.
     */
    NACIONAL("Nacional"),

    /**
     * Produto importado diretamente pelo estabelecimento.
     */
    IMPORTADO_DIRETO("Importado diretamente"),

    /**
     * Produto importado, porém adquirido de fornecedor nacional.
     */
    IMPORTADO_MERCADO_INTERNO("Importado (mercado interno)");

    private final String descricao;

    TipoOrigemProduto(String descricao) {
        this.descricao = descricao;
    }
}
