package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Representa um item de um Pedido de Compra.
 * <p>
 * No ms-compras, o item referencia o produto por identificador lógico ({@code produtoId})
 * e pode manter um snapshot de informações do produto (descrição/unidade) para histórico.
 */
@Entity
@Table(
        name = "item_pedido_compra",
        indexes = {
                @Index(name = "ix_item_pedido_compra_pedido_id", columnList = "pedido_compra_id"),
                @Index(name = "ix_item_pedido_compra_produto_id", columnList = "produto_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ItemPedidoCompra extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Pedido de compra ao qual este item pertence.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_compra_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_pedido_compra_pedido"))
    private PedidoCompra pedidoCompra;

    /**
     * Identificador do produto no ms-produtos (ou catálogo).
     */
    @NotNull
    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    /**
     * Descrição do produto no momento da compra (snapshot).
     */
    @Size(max = 250)
    @Column(name = "produto_descricao_snapshot", length = 250)
    private String produtoDescricaoSnapshot;

    /**
     * Unidade de medida no momento da compra (snapshot), ex.: "UN", "KG".
     */
    @Size(max = 10)
    @Column(name = "unidade_snapshot", length = 10)
    private String unidadeSnapshot;

    /**
     * Quantidade solicitada.
     */
    @NotNull
    @Positive
    @Column(name = "quantidade", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantidade;

    /**
     * Preço unitário negociado.
     */
    @NotNull
    @Column(name = "preco_unitario", nullable = false, precision = 19, scale = 2)
    private BigDecimal precoUnitario;

    /**
     * Desconto aplicado ao item (valor absoluto).
     */
    @NotNull
    @Column(name = "desconto_item", nullable = false, precision = 19, scale = 2)
    private BigDecimal descontoItem;

    /**
     * Total do item (quantidade * preço_unitário - desconto).
     */
    @NotNull
    @Column(name = "total_item", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalItem;

    /**
     * Observações específicas do item (opcional).
     */
    @Size(max = 300)
    @Column(name = "observacao", length = 300)
    private String observacao;
}
