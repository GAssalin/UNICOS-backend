package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que define os tipos possíveis de locais de armazenamento de produtos.
 * <p>
 * Usado para classificar a natureza do local de estoque,
 * facilitando regras de movimentação e relatórios.
 */
@Getter
public enum TipoLocalEstoque {

    /**
     * Estoque central da empresa, normalmente responsável por armazenar grandes volumes.
     */
    DEPOSITO("Depósito central da empresa"),

    /**
     * Estoque vinculado a uma unidade de varejo ou filial comercial.
     */
    LOJA("Estoque de loja ou ponto de venda"),

    /**
     * Estoque mantido por terceiros (parceiros, distribuidores ou consignação).
     */
    TERCEIRO("Estoque de propriedade de terceiros"),

    /**
     * Estoque destinado a produtos em processo de produção ou industrialização.
     */
    PRODUCAO("Estoque de produtos em produção");

    private final String descricao;

    TipoLocalEstoque(String descricao) {
        this.descricao = descricao;
    }
}
