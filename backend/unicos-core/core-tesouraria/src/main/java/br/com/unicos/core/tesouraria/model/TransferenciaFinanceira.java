package br.com.unicos.core.tesouraria.model;

import br.com.unicos.core.tesouraria.enums.MeioPagamento;
import br.com.unicos.core.tesouraria.enums.TipoTransferencia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa uma transferência financeira entre contas.
 *
 * <p>
 * Utilizada para registrar movimentações internas ou externas
 * entre contas bancárias, caixas e aplicações, assegurando
 * a rastreabilidade e controle do fluxo de valores.
 * </p>
 */
@Entity
@Table(name = "transferencia_financeira")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaFinanceira {

    /**
     * Identificador único da transferência.
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
     * Tipo da transferência (ex: Interna, Externa, PIX, TED, DOC).
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, length = 30)
    private TipoTransferencia tipoTransferencia;

    /**
     * Meio de pagamento utilizado para realizar a transferência.
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, length = 30)
    private MeioPagamento meioPagamento;

    /**
     * Valor transferido entre as contas.
     */
    @NotNull
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    /**
     * Data em que a transferência foi efetivamente realizada.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate dataTransferencia;

    /**
     * Observações adicionais sobre a transferência.
     */
    @Column(length = 255)
    private String observacao;

    /**
     * Identificador do responsável pela operação (usuário interno).
     */
    @Column(length = 100)
    private String usuarioResponsavel;
}
