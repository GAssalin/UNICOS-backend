package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Representa uma parcela específica dentro de uma condição de pagamento.
 * <p>
 * Permite modelar condições como "30/60/90", "Entrada + 30/60", etc.
 * Cada parcela define uma ordem, um prazo (dias após emissão) e o percentual do total.
 */
@Entity
@Table(
        name = "condicao_pagamento_parcela",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cond_pag_parcela_condicao_ordem",
                        columnNames = {"condicao_pagamento_id", "ordem"}
                )
        },
        indexes = {
                @Index(name = "ix_cond_pag_parcela_condicao_id", columnList = "condicao_pagamento_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CondicaoPagamentoParcela extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Condição de pagamento à qual esta parcela pertence.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "condicao_pagamento_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cond_pag_parcela_condicao_pagamento")
    )
    private CondicaoPagamento condicaoPagamento;

    /**
     * Ordem da parcela (1..N).
     */
    @NotNull
    @Positive
    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    /**
     * Quantidade de dias após a emissão do documento/pedido para vencimento desta parcela.
     * <p>
     * Ex.: 0 (entrada), 30, 60, 90...
     */
    @NotNull
    @PositiveOrZero
    @Column(name = "dias_apos_emissao", nullable = false)
    private Integer diasAposEmissao;

    /**
     * Percentual do valor total alocado nesta parcela (0..100).
     * <p>
     * Ex.: 33.33 para cada parcela em 3x.
     */
    @NotNull
    @PositiveOrZero
    @Column(name = "percentual", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentual;
}
