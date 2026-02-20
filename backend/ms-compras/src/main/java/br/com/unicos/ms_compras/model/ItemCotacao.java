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
 * Representa um item dentro de uma Cotação de Compra.
 * <p>
 * Cada item referencia um produto do catálogo via {@code produtoId} e registra
 * quantidade e snapshots úteis para histórico e leitura.
 */
@Entity
@Table(
        name = "item_cotacao",
        indexes = {
                @Index(name = "ix_item_cotacao_cotacao_id", columnList = "cotacao_compra_id"),
                @Index(name = "ix_item_cotacao_produto_id", columnList = "produto_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ItemCotacao extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cotação de compra à qual o item pertence.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cotacao_compra_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_cotacao_cotacao"))
    private CotacaoCompra cotacaoCompra;

    /**
     * Identificador do produto no ms-produtos (ou catálogo).
     */
    @NotNull
    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    /**
     * Descrição do produto no momento da cotação (snapshot).
     */
    @Size(max = 250)
    @Column(name = "produto_descricao_snapshot", length = 250)
    private String produtoDescricaoSnapshot;

    /**
     * Unidade do produto no momento da cotação (snapshot), ex.: "UN", "KG".
     */
    @Size(max = 10)
    @Column(name = "unidade_snapshot", length = 10)
    private String unidadeSnapshot;

    /**
     * Quantidade solicitada para cotação.
     */
    @NotNull
    @Positive
    @Column(name = "quantidade", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantidade;

    /**
     * Observações específicas do item (opcional).
     */
    @Size(max = 300)
    @Column(name = "observacao", length = 300)
    private String observacao;
}
