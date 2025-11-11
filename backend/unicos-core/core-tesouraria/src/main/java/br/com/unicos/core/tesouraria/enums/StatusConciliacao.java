package br.com.unicos.core.tesouraria.enums;

import lombok.Getter;

/**
 * Enum que representa o status de uma conciliação bancária.
 * <p>
 * Indica a situação atual da comparação entre saldo bancário e saldo interno do sistema.
 */
@Getter
public enum StatusConciliacao {

    /**
     * Conciliação ainda não validada.
     */
    PENDENTE("Pendente"),

    /**
     * Saldos conciliados com sucesso.
     */
    CONCILIADO("Conciliado"),

    /**
     * Diferença encontrada entre sistema e extrato bancário.
     */
    DIVERGENTE("Divergente");

    private final String descricao;

    StatusConciliacao(String descricao) {
        this.descricao = descricao;
    }
}
