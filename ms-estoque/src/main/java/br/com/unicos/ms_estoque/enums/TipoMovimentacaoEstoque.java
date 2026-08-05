package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que representa o tipo de movimentação realizada no estoque.
 * <p>
 * Define a natureza da operação executada sobre o saldo de um produto.
 */
@Getter
public enum TipoMovimentacaoEstoque {

    /**
     * Entrada de produtos no estoque.
     */
    ENTRADA("Entrada"),

    /**
     * Saída de produtos do estoque.
     */
    SAIDA("Saída"),

    /**
     * Transferência de produtos entre estoques.
     */
    TRANSFERENCIA("Transferência"),

    /**
     * Ajuste positivo de saldo.
     */
    AJUSTE_ENTRADA("Ajuste de entrada"),

    /**
     * Ajuste negativo de saldo.
     */
    AJUSTE_SAIDA("Ajuste de saída"),

    /**
     * Reserva de quantidade em estoque.
     */
    RESERVA("Reserva"),

    /**
     * Liberação de quantidade anteriormente reservada.
     */
    LIBERACAO_RESERVA("Liberação de reserva"),

    /**
     * Movimentação oriunda de inventário.
     */
    INVENTARIO("Inventário"),

    /**
     * Estorno de movimentação anterior.
     */
    ESTORNO("Estorno");

    private final String descricao;

    TipoMovimentacaoEstoque(String descricao) {
        this.descricao = descricao;
    }
}