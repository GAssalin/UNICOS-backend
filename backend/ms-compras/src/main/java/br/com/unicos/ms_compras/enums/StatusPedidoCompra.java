package br.com.unicos.ms_compras.enums;

import br.com.unicos.core.pedido.enums.StatusPedidoBase;
import lombok.Getter;

/**
 * Enum que representa os status específicos do processo de compras.
 *
 * <p>
 * Cada valor reflete uma etapa detalhada do ciclo de vida de um pedido de compra,
 * complementando os status genéricos definidos em {@code StatusPedido}.
 * </p>
 */
@Getter
public enum StatusPedidoCompra implements StatusPedidoBase {

    /**
     * Pedido de compra criado e aguardando cotação com fornecedores.
     */
    AGUARDANDO_COTACAO("Aguardando cotação"),

    /**
     * Pedido em fase de aprovação interna.
     */
    EM_APROVACAO("Em aprovação"),

    /**
     * Pedido aprovado e aguardando envio pelo fornecedor.
     */
    AGUARDANDO_ENTREGA("Aguardando entrega"),

    /**
     * Pedido recebido integralmente no estoque.
     */
    RECEBIDO("Recebido"),

    /**
     * Pedido de compra cancelado.
     */
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPedidoCompra(String descricao) {
        this.descricao = descricao;
    }
}
