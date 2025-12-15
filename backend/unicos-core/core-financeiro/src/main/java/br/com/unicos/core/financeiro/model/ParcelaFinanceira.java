package br.com.unicos.core.financeiro.model;

import br.com.unicos.core.financeiro.enums.StatusFinanceiro;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidade que representa uma parcela vinculada a um lançamento financeiro.
 * <p>
 * Utilizada em pagamentos ou recebimentos parcelados,
 * armazenando informações sobre vencimento, valor e status da parcela.
 */
@Entity
@Table(name = "parcela_financeira")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelaFinanceira {

    /**
     * Identificador único da parcela financeira.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número identificador da parcela (ex: 1, 2, 3...).
     */
    @NotNull
    @Column(nullable = false)
    private Integer numeroParcela;

    /**
     * Valor da parcela.
     */
    @NotNull
    @Column(nullable = false)
    private Double valor;

    /**
     * Data de vencimento da parcela.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate dataVencimento;

    /**
     * Data em que a parcela foi efetivamente paga (caso aplicável).
     */
    @Column
    private LocalDate dataPagamento;

    /**
     * Situação atual da parcela financeira.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusFinanceiro status;

    /**
     * Lançamento financeiro ao qual esta parcela pertence.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lancamento_financeiro_id", nullable = false)
    private LancamentoFinanceiro lancamentoFinanceiro;
}
