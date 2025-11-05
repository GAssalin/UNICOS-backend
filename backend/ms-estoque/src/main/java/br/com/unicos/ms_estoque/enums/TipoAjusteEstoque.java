package br.com.unicos.ms_estoque.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de ajustes que podem ocorrer no estoque.
 * <p>
 * Auxilia na classificação e auditoria dos ajustes manuais realizados
 * durante processos de inventário ou correções de saldo.
 */
@Getter
public enum TipoAjusteEstoque {

    /**
     * Ajuste para aumentar a quantidade de um produto no estoque.
     */
    AJUSTE_POSITIVO("Ajuste positivo (aumento de quantidade)"),

    /**
     * Ajuste para reduzir a quantidade de um produto no estoque.
     */
    AJUSTE_NEGATIVO("Ajuste negativo (redução de quantidade)"),

    /**
     * Ajuste resultante de contagem divergente em inventário físico.
     */
    AJUSTE_INVENTARIO("Ajuste decorrente de inventário físico");

    private final String descricao;

    TipoAjusteEstoque(String descricao) {
        this.descricao = descricao;
    }
}
