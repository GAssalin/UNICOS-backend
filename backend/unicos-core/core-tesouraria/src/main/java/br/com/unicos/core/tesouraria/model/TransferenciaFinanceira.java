package br.com.unicos.core.tesouraria.model;

import br.com.unicos.core.tesouraria.enums.TipoTransferencia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa uma transferência de valores entre contas financeiras.
 * <p>
 * Utilizada para registrar movimentações internas ou ajustes contábeis
 * entre contas da empresa.
 */
@Entity
@Table(name = "transferencia_financeira")
@Getter
@Setter
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
    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private ContaFinanceira contaOrigem;

    /**
     * Conta de destino da transferência.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_destino_id", nullable = false)
    private ContaFinanceira contaDestino;

    /**
     * Tipo da transferência (interna, externa ou ajuste).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoTransferencia tipo;

    /**
     * Valor transferido entre as contas.
     */
    @NotNull
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    /**
     * Data em que a transferência foi realizada.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate dataTransferencia;

    /**
     * Observações complementares sobre a operação.
     */
    @Column(length = 255)
    private String observacao;
}
