package br.com.unicos.core.tesouraria.model;

import br.com.unicos.core.tesouraria.enums.StatusConciliacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa a conciliação bancária de uma conta financeira.
 *
 * <p>
 * Permite confrontar os lançamentos financeiros internos com os registros do
 * extrato bancário, identificando valores conciliados, divergências e status
 * da conciliação.
 * </p>
 */
@Entity
@Table(name = "conciliacao_bancaria")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConciliacaoBancaria {

    /**
     * Identificador único da conciliação bancária.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Conta financeira à qual a conciliação pertence.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_financeira_id", nullable = false)
    private ContaFinanceira contaFinanceira;

    /**
     * Lançamento financeiro associado à conciliação, quando aplicável.
     */
    @OneToOne
    @JoinColumn(name = "lancamento_financeiro_id")
    private LancamentoFinanceiro lancamentoFinanceiro;

    /**
     * Status atual da conciliação (ex: Pendente, Conciliado, Divergente).
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, length = 30)
    private StatusConciliacao status;

    /**
     * Data em que a conciliação foi realizada.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate dataConciliacao;

    /**
     * Valor conciliado entre os registros internos e o extrato bancário.
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal valorConciliado;

    /**
     * Diferença encontrada na conciliação, quando houver.
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal diferenca;

    /**
     * Observações adicionais sobre a conciliação.
     */
    @Column(length = 255)
    private String observacao;
}
