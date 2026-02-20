package br.com.unicos.ms_compras.enums;

import lombok.Getter;

/**
 * Representa o status do Pedido de Compra.
 * <p>
 * Controla o fluxo do pedido dentro do processo de compras.
 */
@Getter
public enum StatusPedidoCompra {

    /**
     * Pedido ainda em elaboração.
     */
    RASCUNHO("Rascunho"),

    /**
     * Pedido aguardando análise/aprovação interna.
     */
    EM_ANALISE("Em análise"),

    /**
     * Pedido aprovado internamente.
     */
    APROVADO("Aprovado"),

    /**
     * Pedido enviado ao fornecedor.
     */
    ENVIADO_FORNECEDOR("Enviado ao fornecedor"),

    /**
     * Pedido parcialmente recebido.
     */
    PARCIALMENTE_RECEBIDO("Parcialmente recebido"),

    /**
     * Pedido totalmente recebido.
     */
    RECEBIDO("Recebido"),

    /**
     * Pedido cancelado.
     */
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPedidoCompra(String descricao) {
        this.descricao = descricao;
    }
}
