package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um item recebido durante o recebimento da compra.
 * <p>
 * Pode referenciar diretamente o item do pedido para permitir conferência item a item.
 */
@Entity
@Table(
        name = "item_recebimento_compra",
        indexes = {
                @Index(name = "ix_item_recebimento_compra_recebimento_id", columnList = "recebimento_compra_id"),
                @Index(name = "ix_item_recebimento_compra_item_pedido_id", columnList = "item_pedido_compra_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ItemRecebimentoCompra extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Recebimento ao qual este item pertence.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recebimento_compra_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_recebimento_compra_recebimento"))
    private RecebimentoCompra recebimentoCompra;

    /**
     * Item do pedido de compra que está sendo recebido.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_pedido_compra_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_recebimento_compra_item_pedido"))
    private ItemPedidoCompra itemPedidoCompra;

    /**
     * Quantidade recebida.
     */
    @NotNull
    @PositiveOrZero
    @Column(name = "quantidade_recebida", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantidadeRecebida;

    /**
     * Quantidade aprovada após conferência (opcional no MVP, mas útil).
     */
    @NotNull
    @PositiveOrZero
    @Column(name = "quantidade_aprovada", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantidadeAprovada;

    /**
     * Quantidade recusada (avaria, item errado etc.).
     */
    @NotNull
    @PositiveOrZero
    @Column(name = "quantidade_recusada", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantidadeRecusada;

    /**
     * Observação do item no recebimento.
     */
    @Size(max = 300)
    @Column(name = "observacao", length = 300)
    private String observacao;

    /**
     * Divergências registradas para este item.
     */
    @Valid
    @NotNull
    @Builder.Default
    @OneToMany(mappedBy = "itemRecebimentoCompra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DivergenciaRecebimento> divergencias = new ArrayList<>();
}
