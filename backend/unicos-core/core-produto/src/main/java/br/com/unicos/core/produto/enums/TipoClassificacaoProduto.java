package br.com.unicos.core.produto.enums;

import lombok.Getter;

/**
 * Enum que representa a classificação comercial ou qualitativa de um produto.
 *
 * <p>
 * Utilizado para categorizar o posicionamento do item no mercado,
 * auxiliar estratégias de preço, segmentação e relatórios gerenciais.
 * </p>
 */
@Getter
public enum TipoClassificacaoProduto {

    /**
     * Produto genérico, sem uma diferenciação significativa.
     */
    GENERICO("Genérico"),

    /**
     * Produto com qualidade superior e melhor acabamento.
     */
    PREMIUM("Premium"),

    /**
     * Produto voltado ao mercado de alto padrão.
     */
    LUXO("Luxo"),

    /**
     * Produto econômico, de consumo popular.
     */
    POPULAR("Popular"),

    /**
     * Produto destinado ao uso industrial ou técnico.
     */
    INDUSTRIAL("Industrial"),

    /**
     * Produto alimentício ou relacionado à alimentação.
     */
    ALIMENTICIO("Alimentício");

    private final String descricao;

    TipoClassificacaoProduto(String descricao) {
        this.descricao = descricao;
    }
}
