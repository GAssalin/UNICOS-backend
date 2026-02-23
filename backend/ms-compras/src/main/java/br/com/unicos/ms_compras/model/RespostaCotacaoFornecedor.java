package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a resposta de um fornecedor para uma cotação específica.
 * <p>
 * Um fornecedor pode responder uma cotação informando preços e condições por item.
 */
@Entity
@Table(
        name = "resposta_cotacao_fornecedor",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_resposta_cotacao_fornecedor_cotacao_fornecedor",
                        columnNames = {"cotacao_compra_id", "fornecedor_id"}
                )
        },
        indexes = {
                @Index(name = "ix_resposta_cotacao_fornecedor_cotacao_id", columnList = "cotacao_compra_id"),
                @Index(name = "ix_resposta_cotacao_fornecedor_fornecedor_id", columnList = "fornecedor_id"),
                @Index(name = "ix_resposta_cotacao_fornecedor_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class RespostaCotacaoFornecedor extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cotação que está sendo respondida.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cotacao_compra_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_resposta_cotacao_fornecedor_cotacao"))
    private CotacaoCompra cotacaoCompra;

    /**
     * Fornecedor que respondeu a cotação.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_resposta_cotacao_fornecedor_fornecedor"))
    private Fornecedor fornecedor;

    /**
     * Status da resposta (MVP como texto: "ENVIADA", "PENDENTE", "RECUSADA", "VENCIDA"...).
     * <p>
     * Se você tiver enum, trocamos para @Enumerated(EnumType.STRING).
     */
    @NotNull
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    /**
     * Data/hora em que o fornecedor respondeu.
     */
    @Column(name = "respondido_em")
    private LocalDateTime respondidoEm;

    /**
     * Condição de pagamento proposta (opcional).
     * <p>
     * Se CondicaoPagamento for do ms-compras, mantém relacionamento.
     * Se for do ms-financeiro, melhor trocar para condicaoPagamentoId.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condicao_pagamento_id",
            foreignKey = @ForeignKey(name = "fk_resposta_cotacao_fornecedor_condicao_pagamento"))
    private CondicaoPagamento condicaoPagamento;

    /**
     * Valor total proposto (soma dos itens - descontos + frete, se aplicável).
     */
    @NotNull
    @Column(name = "total_proposto", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalProposto;

    /**
     * Valor de frete proposto (opcional).
     */
    @NotNull
    @Column(name = "frete", nullable = false, precision = 19, scale = 2)
    private BigDecimal frete;

    /**
     * Observações do fornecedor (opcional).
     */
    @Size(max = 500)
    @Column(name = "observacao", length = 500)
    private String observacao;

    /**
     * Itens com preços propostos para cada item da cotação.
     */
    @Valid
    @NotNull
    @Builder.Default
    @OneToMany(mappedBy = "respostaCotacaoFornecedor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemRespostaCotacaoFornecedor> itens = new ArrayList<>();
}
