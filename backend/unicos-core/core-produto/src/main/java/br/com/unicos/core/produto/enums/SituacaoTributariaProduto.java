package br.com.unicos.core.produto.enums;

import lombok.Getter;

/**
 * Enum que representa a situação tributária do produto.
 *
 * <p>
 * Utilizada em integrações fiscais para definir o enquadramento do produto
 * quanto à tributação de ICMS, IPI e outros impostos, garantindo conformidade
 * entre os módulos Fiscal, Compras e Vendas.
 * </p>
 */
@Getter
public enum SituacaoTributariaProduto {

    /**
     * Produto tributado integralmente.
     */
    TRIBUTADO("Tributado"),

    /**
     * Produto isento de tributação.
     */
    ISENTO("Isento"),

    /**
     * Produto sujeito à substituição tributária.
     */
    SUBSTITUICAO_TRIBUTARIA("Substituição tributária"),

    /**
     * Produto com redução de base de cálculo.
     */
    REDUCAO_BASE_CALCULO("Redução de base de cálculo"),

    /**
     * Produto não tributado ou sem incidência.
     */
    NAO_TRIBUTADO("Não tributado");

    private final String descricao;

    SituacaoTributariaProduto(String descricao) {
        this.descricao = descricao;
    }
}
