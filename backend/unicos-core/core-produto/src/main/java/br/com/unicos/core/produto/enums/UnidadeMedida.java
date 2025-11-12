package br.com.unicos.core_produto.enums;

import lombok.Getter;

/**
 * Enum que representa as unidades de medida padronizadas no UniCoS.
 *
 * <p>
 * Utilizada em diversos módulos (Produtos, Compras, Estoque e Fiscal)
 * para garantir consistência na representação de quantidades e pesos.
 * </p>
 */
@Getter
public enum UnidadeMedida {

    /**
     * Unidade (peça, item individual).
     */
    UNIDADE("Unidade"),

    /**
     * Quilograma.
     */
    QUILOGRAMA("Quilograma"),

    /**
     * Grama.
     */
    GRAMA("Grama"),

    /**
     * Litro.
     */
    LITRO("Litro"),

    /**
     * Mililitro.
     */
    MILILITRO("Mililitro"),

    /**
     * Metro.
     */
    METRO("Metro"),

    /**
     * Centímetro.
     */
    CENTIMETRO("Centímetro"),

    /**
     * Caixa com múltiplas unidades.
     */
    CAIXA("Caixa"),

    /**
     * Pacote com múltiplas unidades.
     */
    PACOTE("Pacote");

    private final String descricao;

    UnidadeMedida(String descricao) {
        this.descricao = descricao;
    }
}
