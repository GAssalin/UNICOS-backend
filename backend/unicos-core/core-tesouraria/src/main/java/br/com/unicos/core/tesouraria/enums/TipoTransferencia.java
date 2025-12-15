package br.com.unicos.core.tesouraria.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de transferência financeira entre contas.
 * <p>
 * Indica a finalidade ou origem da movimentação.
 */
@Getter
public enum TipoTransferencia {

    /**
     * Transferência entre contas da mesma empresa.
     */
    INTERNA("Transferência interna"),

    /**
     * Transferência para conta externa.
     */
    EXTERNA("Transferência externa"),

    /**
     * Ajuste interno contábil.
     */
    AJUSTE("Ajuste interno");

    private final String descricao;

    TipoTransferencia(String descricao) {
        this.descricao = descricao;
    }
}
