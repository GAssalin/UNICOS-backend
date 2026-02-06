package br.com.unicos.core.produto.model.enums;

import lombok.Getter;

/**
 * Enum que representa o status operacional do produto dentro do sistema.
 *
 * <p>
 * O status define se o produto pode ser vendido, comprado ou movimentado
 * no estoque, além de auxiliar em filtros e relatórios.
 * </p>
 */
@Getter
public enum StatusProduto {

    /**
     * Produto ativo e disponível para uso no sistema.
     */
    ATIVO("Ativo"),

    /**
     * Produto desativado temporariamente, não podendo ser movimentado.
     */
    INATIVO("Inativo"),

    /**
     * Produto que não é mais comercializado ou produzido.
     */
    DESCONTINUADO("Descontinuado"),

    /**
     * Produto bloqueado por motivo operacional ou restrição administrativa.
     */
    BLOQUEADO("Bloqueado");

    private final String descricao;

    StatusProduto(String descricao) {
        this.descricao = descricao;
    }
}
