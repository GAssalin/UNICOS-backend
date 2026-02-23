package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma Cotação de Compra (solicitação de preços para um conjunto de itens).
 * <p>
 * Uma cotação pode ser originada de uma requisição interna e, após escolhida a melhor proposta,
 * pode gerar um Pedido de Compra.
 */
@Entity
@Table(
        name = "cotacao_compra",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cotacao_compra_codigo", columnNames = {"codigo"})
        },
        indexes = {
                @Index(name = "ix_cotacao_compra_status", columnList = "status"),
                @Index(name = "ix_cotacao_compra_data_abertura", columnList = "data_abertura"),
                @Index(name = "ix_cotacao_compra_data_validade", columnList = "data_validade")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CotacaoCompra extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código interno da cotação (ex.: "COT-2026-0001").
     */
    @NotBlank
    @Size(max = 30)
    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    /**
     * Data de abertura da cotação.
     */
    @NotNull
    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    /**
     * Data de validade da cotação (prazo para fornecedores responderem).
     */
    @NotNull
    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    /**
     * Status da cotação (MVP como texto: "ABERTA", "ENCERRADA", "CANCELADA"...).
     * <p>
     * Se você tiver enum, trocamos para @Enumerated(EnumType.STRING).
     */
    @NotBlank
    @Size(max = 30)
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    /**
     * Observações/escopo da cotação (opcional).
     */
    @Size(max = 500)
    @Column(name = "observacao", length = 500)
    private String observacao;

    /**
     * Pedido de compra gerado a partir desta cotação (opcional).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_compra_id",
            foreignKey = @ForeignKey(name = "fk_cotacao_compra_pedido_compra"))
    private PedidoCompra pedidoCompra;

    /**
     * Itens cotados.
     */
    @Valid
    @NotNull
    @Builder.Default
    @OneToMany(mappedBy = "cotacaoCompra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemCotacao> itens = new ArrayList<>();
}
