package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa o recebimento de uma compra (entrada/conferência) vinculada a um Pedido de Compra.
 * <p>
 * É o agregado que registra o que foi efetivamente recebido, divergências e documentos de entrada.
 */
@Entity
@Table(
        name = "recebimento_compra",
        indexes = {
                @Index(name = "ix_recebimento_compra_pedido_id", columnList = "pedido_compra_id"),
                @Index(name = "ix_recebimento_compra_fornecedor_id", columnList = "fornecedor_id"),
                @Index(name = "ix_recebimento_compra_status", columnList = "status"),
                @Index(name = "ix_recebimento_compra_data_recebimento", columnList = "data_recebimento")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class RecebimentoCompra extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Pedido de compra ao qual este recebimento está vinculado.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_compra_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_recebimento_compra_pedido"))
    private PedidoCompra pedidoCompra;

    /**
     * Fornecedor (redundante em relação ao pedido, mas útil para consulta e integridade histórica).
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_recebimento_compra_fornecedor"))
    private Fornecedor fornecedor;

    /**
     * Data em que o recebimento ocorreu.
     */
    @NotNull
    @Column(name = "data_recebimento", nullable = false)
    private LocalDate dataRecebimento;

    /**
     * Status do recebimento (MVP texto: "EM_CONFERENCIA", "CONCLUIDO", "CANCELADO"...).
     */
    @NotNull
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    /**
     * Observações gerais da conferência/recebimento.
     */
    @Size(max = 500)
    @Column(name = "observacao", length = 500)
    private String observacao;

    /**
     * Itens recebidos.
     */
    @Valid
    @NotNull
    @Builder.Default
    @OneToMany(mappedBy = "recebimentoCompra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemRecebimentoCompra> itens = new ArrayList<>();

    /**
     * Documentos de entrada vinculados ao recebimento (NF, recibo, etc.).
     */
    @Valid
    @NotNull
    @Builder.Default
    @OneToMany(mappedBy = "recebimentoCompra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DocumentoEntrada> documentosEntrada = new ArrayList<>();
}
