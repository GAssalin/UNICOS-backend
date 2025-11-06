package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Enum que representa os estágios do processo de recebimento de mercadorias.
 *
 * <p>Utilizado para controlar o fluxo de conferência, entrada e finalização do recebimento.</p>
 */
@Getter
public enum StatusRecebimentoCompra {

    /**
     * Recebimento criado, aguardando chegada das mercadorias.
     */
    AGUARDANDO_ENTREGA("Recebimento aguardando chegada das mercadorias."),

    /**
     * Produtos recebidos fisicamente, mas ainda não conferidos.
     */
    PENDENTE_CONFERENCIA("Produtos recebidos, pendentes de conferência física e documental."),

    /**
     * Recebimento parcialmente conferido.
     */
    CONFERENCIA_PARCIAL("Conferência parcial realizada, itens pendentes."),

    /**
     * Recebimento totalmente conferido e validado.
     */
    CONFERIDO("Conferência finalizada com sucesso."),

    /**
     * Recebimento cancelado antes da conclusão.
     */
    CANCELADO("Recebimento cancelado antes da conclusão do processo."),

    /**
     * Recebimento finalizado e integrado ao estoque.
     */
    FINALIZADO("Recebimento finalizado, com movimentação de estoque registrada.");

    private final String descricao;

    StatusRecebimentoCompra(String descricao) {
        this.descricao = descricao;
    }
}
