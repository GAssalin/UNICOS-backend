package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Representa uma divergência identificada no recebimento de um item.
 * <p>
 * Exemplos: quantidade menor/maior, avaria, produto divergente, embalagem violada, etc.
 */
@Entity
@Table(
        name = "divergencia_recebimento",
        indexes = {
                @Index(name = "ix_divergencia_recebimento_item_recebimento_id", columnList = "item_recebimento_compra_id"),
                @Index(name = "ix_divergencia_recebimento_tipo", columnList = "tipo")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class DivergenciaRecebimento extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Item do recebimento ao qual a divergência pertence.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_recebimento_compra_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_divergencia_recebimento_item_recebimento"))
    private ItemRecebimentoCompra itemRecebimentoCompra;

    /**
     * Tipo da divergência (MVP texto: "QUANTIDADE", "AVARIA", "ITEM_ERRADO"...).
     * <p>
     * Se você tiver enum, trocamos para @Enumerated.
     */
    @NotBlank
    @Size(max = 30)
    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    /**
     * Descrição detalhada da divergência.
     */
    @NotBlank
    @Size(max = 500)
    @Column(name = "descricao", nullable = false, length = 500)
    private String descricao;

    /**
     * Quantidade divergente (quando aplicável).
     */
    @PositiveOrZero
    @Column(name = "quantidade_divergente", precision = 19, scale = 4)
    private BigDecimal quantidadeDivergente;
}
