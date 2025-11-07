package br.com.unicos.ms_compras.model.pedido;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidade que representa um item de um pedido de compra.
 *
 * <p>
 * Detalha os produtos ou serviços adquiridos, incluindo quantidade,
 * preço unitário, desconto e vínculo com o pedido principal.
 * </p>
 */
@Entity
@Table(name = "pedido_item_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItemCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador do produto comprado (referência ao ms-produtos).
     */
    @NotNull
    private Long produtoId;

    /**
     * Quantidade adquirida do produto.
     */
    @NotNull
    private BigDecimal quantidade;

    /**
     * Valor unitário negociado para o item.
     */
    @NotNull
    @Column(precision = 15, scale = 2)
    private BigDecimal precoUnitario;

    /**
     * Valor total do item (quantidade × preço unitário - desconto).
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal valorTotal;

    /**
     * Percentual ou valor de desconto aplicado neste item.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal desconto;

    /**
     * Pedido de compra ao qual este item pertence.
     */
    @ManyToOne
    @JoinColumn(name = "pedido_compra_id")
    private PedidoCompra pedidoCompra;
}
