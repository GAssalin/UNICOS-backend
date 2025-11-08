package br.com.unicos.ms_vendas.enums;

import br.com.unicos.core.pedido.enums.StatusPedidoBase;
import lombok.Getter;

/**
 * Enum que representa o status atual de um pedido de compra.
 * <p>
 * Indica em qual etapa do fluxo de processamento o pedido se encontra.
 */
@Getter
public enum StatusPedidoVenda implements StatusPedidoBase {

    /**
     * Pedido criado, mas ainda não aprovado.
     */
    ABERTO("Aberto"),

    /**
     * Pedido aprovado internamente para emissão ao fornecedor.
     */
    APROVADO("Aprovado"),

    /**
     * Pedido enviado ao fornecedor.
     */
    ENVIADO_FORNECEDOR("Enviado ao fornecedor"),

    /**
     * Pedido recebido parcial ou totalmente pela empresa.
     */
    RECEBIDO("Recebido"),

    /**
     * Pedido cancelado por solicitação, erro ou reprovação.
     */
    CANCELADO("Cancelado"),

    /**
     * Pedido pago e concluído.
     */
    FINALIZADO("Finalizado");

    private final String descricao;

    StatusPedidoVenda(String descricao) {
        this.descricao = descricao;
    }
}
