package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_compras.enums.MotivoCancelamentoCompra;
import br.com.unicos.ms_compras.enums.StatusPedidoCompra;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um Pedido de Compra no contexto do microserviço ms-compras.
 * <p>
 * É o agregado central do processo de compra, contendo fornecedor, itens,
 * totais, status e informações de aprovação/cancelamento.
 */
@Entity
@Table(
        name = "pedido_compra",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_pedido_compra_codigo", columnNames = {"codigo"})
        },
        indexes = {
                @Index(name = "ix_pedido_compra_fornecedor_id", columnList = "fornecedor_id"),
                @Index(name = "ix_pedido_compra_status", columnList = "status_pedido_compra"),
                @Index(name = "ix_pedido_compra_data_emissao", columnList = "data_emissao")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PedidoCompra extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código interno do pedido de compra (ex.: "PC-2026-0001").
     */
    @NotBlank
    @Size(max = 30)
    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    /**
     * Fornecedor vinculado ao pedido.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pedido_compra_fornecedor"))
    private Fornecedor fornecedor;

    /**
     * Data de emissão do pedido.
     */
    @NotNull
    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    /**
     * Data prevista para entrega (opcional).
     */
    @Column(name = "data_prevista_entrega")
    private LocalDate dataPrevistaEntrega;

    /**
     * Status atual do pedido.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido_compra", nullable = false, length = 30)
    private StatusPedidoCompra statusPedidoCompra;

    /**
     * Condição de pagamento aplicada ao pedido (opcional no MVP).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condicao_pagamento_id",
            foreignKey = @ForeignKey(name = "fk_pedido_compra_condicao_pagamento"))
    private CondicaoPagamento condicaoPagamento;

    /**
     * Observações gerais do pedido (opcional).
     */
    @Size(max = 500)
    @Column(name = "observacao", length = 500)
    private String observacao;

    /**
     * Totais do pedido (campos persistidos para facilitar consulta e relatórios).
     */
    @NotNull
    @Column(name = "subtotal", nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal;

    @NotNull
    @Column(name = "desconto", nullable = false, precision = 19, scale = 2)
    private BigDecimal desconto;

    @NotNull
    @Column(name = "frete", nullable = false, precision = 19, scale = 2)
    private BigDecimal frete;

    @NotNull
    @Column(name = "total", nullable = false, precision = 19, scale = 2)
    private BigDecimal total;

    /**
     * Dados de aprovação (opcional; útil quando houver workflow simples).
     */
    @Column(name = "aprovado_por")
    private Long aprovadoPor;

    @Column(name = "aprovado_em")
    private LocalDateTime aprovadoEm;

    /**
     * Dados de cancelamento.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_cancelamento", length = 40)
    private MotivoCancelamentoCompra motivoCancelamentoCompra;

    @Size(max = 300)
    @Column(name = "observacao_cancelamento", length = 300)
    private String observacaoCancelamento;

    @Column(name = "cancelado_por")
    private Long canceladoPor;

    @Column(name = "cancelado_em")
    private LocalDateTime canceladoEm;

    /**
     * Itens do pedido de compra.
     */
    @Valid
    @NotNull
    @Builder.Default
    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedidoCompra> itens = new ArrayList<>();
}
