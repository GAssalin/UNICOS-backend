package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que representa o tipo de saldo controlado no estoque.
 */
@Getter
public enum TipoSaldoEstoque {

    /**
     * Saldo físico total do produto no estoque.
     */
    ATUAL("Atual"),

    /**
     * Saldo reservado para alguma operação.
     */
    RESERVADO("Reservado"),

    /**
     * Saldo disponível para novas movimentações.
     */
    DISPONIVEL("Disponível");

    private final String descricao;

    TipoSaldoEstoque(String descricao) {
        this.descricao = descricao;
    }
}