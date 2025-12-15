package br.com.unicos.core.produto.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de produtos utilizados no sistema.
 *
 * <p>
 * Define a classificação funcional do produto dentro da empresa, auxiliando
 * na organização, tributação e regras de negócio de compras, estoque e vendas.
 * </p>
 */
@Getter
public enum TipoProduto {

    /**
     * Produto adquirido de terceiros para revenda direta.
     */
    MERCADORIA_PARA_REVENDA("Mercadoria para Revenda"),

    /**
     * Insumo utilizado no processo produtivo.
     */
    MATERIA_PRIMA("Matéria-prima"),

    /**
     * Produto resultante do processo de fabricação interna.
     */
    PRODUTO_ACABADO("Produto Acabado"),

    /**
     * Embalagens utilizadas para acondicionamento interno ou venda.
     */
    EMBALAGEM("Embalagem"),

    /**
     * Serviços prestados pela empresa.
     */
    SERVICO("Serviço"),

    /**
     * Produtos utilizados internamente pela empresa, não destinados à venda.
     */
    USO_E_CONSUMO("Uso e Consumo");

    private final String descricao;

    TipoProduto(String descricao) {
        this.descricao = descricao;
    }
}
