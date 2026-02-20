package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Representa o preço e condições propostas pelo fornecedor para um item específico da cotação.
 */
@Entity
@Table(
        name = "item_resposta_cotacao_fornecedor",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_item_resposta_cotacao_fornecedor_resposta_itemcotacao",
                        columnNames = {"resposta_cotacao_fornecedor_id", "item_cotacao_id"}
                )
        },
        indexes = {
                @Index(name = "ix_item_resposta_cotacao_fornecedor_resposta_id", columnList = "resposta_cotacao_fornecedor_id"),
                @Index(name = "ix_item_resposta_cotacao_fornecedor_item_cotacao_id", columnList = "item_cotacao_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ItemRespostaCotacaoFornecedor extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Resposta do fornecedor à qual este item pertence.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resposta_cotacao_fornecedor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_resposta_cotacao_fornecedor_resposta"))
    private RespostaCotacaoFornecedor respostaCotacaoFornecedor;

    /**
     * Item da cotação que está sendo precificado.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_cotacao_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_resposta_cotacao_fornecedor_item_cotacao"))
    private ItemCotacao itemCotacao;

    /**
     * Preço unitário proposto pelo fornecedor.
     */
    @NotNull
    @PositiveOrZero
    @Column(name = "preco_unitario", nullable = false, precision = 19, scale = 2)
    private BigDecimal precoUnitario;

    /**
     * Desconto proposto para o item (valor absoluto).
     */
    @NotNull
    @PositiveOrZero
    @Column(name = "desconto_item", nullable = false, precision = 19, scale = 2)
    private BigDecimal descontoItem;

    /**
     * Total do item proposto (quantidade * preço - desconto).
     */
    @NotNull
    @PositiveOrZero
    @Column(name = "total_item", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalItem;

    /**
     * Prazo estimado de entrega em dias (opcional).
     */
    @Column(name = "prazo_entrega_dias")
    private Integer prazoEntregaDias;

    /**
     * Observações do fornecedor para este item (opcional).
     */
    @Size(max = 300)
    @Column(name = "observacao", length = 300)
    private String observacao;
}
