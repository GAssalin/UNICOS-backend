package br.com.unicos.core.pedido.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Representa um item que compõe um pedido dentro do sistema UniCoS.
 *
 * <p>
 * Cada item refere-se a um produto específico, com quantidade e valor unitário,
 * permitindo o cálculo do subtotal do pedido.
 * </p>
 */
@Entity
@Table(name = "item_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido {

    /**
     * Identificador único do item de pedido.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador do produto relacionado ao item.
     * <p>
     * Relaciona-se a um produto cadastrado em {@code ms-produtos}.
     * </p>
     */
    @Column(nullable = false)
    private Long produtoId;

    /**
     * Quantidade de produtos incluídos neste item.
     */
    @Column(nullable = false)
    private Integer quantidade;

    /**
     * Valor unitário do produto no momento da inclusão no pedido.
     */
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valorUnitario;

    /**
     * Valor total deste item (quantidade * valor unitário).
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;
}
