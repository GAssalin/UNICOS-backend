package br.com.unicos.ms_vendas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidade que representa um item do pedido de venda.
 *
 * <p>
 * Detalha os produtos ou serviços vendidos, com quantidade, preço e desconto aplicável.
 * </p>
 */
@Entity
@Table(name = "pedido_item_venda")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador do produto vendido (referência ao ms-produtos).
     */
    @NotNull
    private Long produtoId;

    /**
     * Quantidade do produto vendida.
     */
    @NotNull
    private BigDecimal quantidade;

    /**
     * Valor unitário do item no momento da venda.
     */
    @NotNull
    @Column(precision = 15, scale = 2)
    private BigDecimal precoUnitario;

    /**
     * Desconto aplicado no item, caso exista.
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal desconto;

    /**
     * Relação com o pedido de venda.
     */
    @ManyToOne
    @JoinColumn(name = "pedido_venda_id")
    private PedidoVenda pedidoVenda;
}
