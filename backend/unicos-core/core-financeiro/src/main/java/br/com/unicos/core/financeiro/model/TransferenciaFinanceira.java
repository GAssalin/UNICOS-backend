package br.com.unicos.core.financeiro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidade que representa uma transferência de valores entre contas financeiras.
 * <p>
 * Utilizada para registrar movimentações internas de recursos,
 * como transferências entre bancos, filiais ou contas da mesma empresa.
 */
@Entity
@Table(name = "transferencia_financeira")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaFinanceira {

    /**
     * Identificador único da transferência financeira.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Conta de origem da transferência.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private ContaFinanceira contaOrigem;

    /**
     * Conta de destino da transferência.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id", nullable = false)
    private ContaFinanceira contaDestino;

    /**
     * Valor transferido entre as contas.
     */
    @NotNull
    @Column(nullable = false)
    private Double valor;

    /**
     * Data em que a transferência foi realizada.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate dataTransferencia;

    /**
     * Observação ou descrição complementar sobre a transferência.
     */
    @Column(length = 255)
    private String observacao;
}
