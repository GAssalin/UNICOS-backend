package br.com.unicos.core.financeiro.enums;

import lombok.Getter;

/**
 * Enum que representa os tipos de lançamentos financeiros.
 * <p>
 * Indica se o registro corresponde a uma receita, despesa ou outro tipo de movimentação.
 */
@Getter
public enum TipoLancamento {

    /**
     * Lançamento referente a entrada de valores (receita).
     */
    RECEITA("Receita"),

    /**
     * Lançamento referente à saída de valores (despesa).
     */
    DESPESA("Despesa"),

    /**
     * Lançamento interno entre contas da mesma empresa.
     */
    TRANSFERENCIA("Transferência"),

    /**
     * Ajuste manual para correção de valores.
     */
    AJUSTE("Ajuste");

    private final String descricao;

    TipoLancamento(String descricao) {
        this.descricao = descricao;
    }

}
