package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que representa a origem de uma movimentação de estoque.
 * <p>
 * Identifica qual processo ou contexto gerou a movimentação.
 */
@Getter
public enum TipoOrigemMovimentacaoEstoque {

    /**
     * Movimentação gerada manualmente por usuário.
     */
    MANUAL("Manual"),

    /**
     * Movimentação originada por processo de venda.
     */
    VENDA("Venda"),

    /**
     * Movimentação originada por compra.
     */
    COMPRA("Compra"),

    /**
     * Movimentação originada por transferência entre estoques.
     */
    TRANSFERENCIA("Transferência"),

    /**
     * Movimentação originada por inventário.
     */
    INVENTARIO("Inventário"),

    /**
     * Movimentação originada por devolução.
     */
    DEVOLUCAO("Devolução"),

    /**
     * Movimentação originada por ajuste administrativo.
     */
    AJUSTE("Ajuste"),

    /**
     * Movimentação originada por integração externa.
     */
    INTEGRACAO("Integração");

    private final String descricao;

    TipoOrigemMovimentacaoEstoque(String descricao) {
        this.descricao = descricao;
    }
}