package br.com.unicos.core.financeiro.model;

import br.com.unicos.core.financeiro.enums.StatusFinanceiro;
import br.com.unicos.core.financeiro.enums.TipoLancamento;
import br.com.unicos.core.financeiro.enums.TipoMovimentoFinanceiro;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidade que representa um lançamento financeiro genérico.
 * <p>
 * Utilizada para registrar movimentações de entrada ou saída de valores,
 * sendo a base para operações de Contas a Pagar, Contas a Receber e Tesouraria.
 */
@Entity
@Table(name = "lancamento_financeiro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoFinanceiro {

    /**
     * Identificador único do lançamento financeiro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Descrição resumida do lançamento.
     */
    @NotBlank
    @Column(nullable = false, length = 150)
    private String descricao;

    /**
     * Tipo do lançamento (ex: receita, despesa, transferência).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoLancamento tipoLancamento;

    /**
     * Tipo de movimento financeiro (crédito ou débito).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimentoFinanceiro tipoMovimento;

    /**
     * Valor total do lançamento.
     */
    @NotNull
    @Column(nullable = false)
    private Double valor;

    /**
     * Data de competência (mês/ano em que o valor é contabilizado).
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate dataCompetencia;

    /**
     * Data de vencimento do lançamento.
     */
    @Column
    private LocalDate dataVencimento;

    /**
     * Situação atual do lançamento financeiro.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusFinanceiro status;

    /**
     * Conta financeira associada ao lançamento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_financeira_id", nullable = false)
    private ContaFinanceira contaFinanceira;

    /**
     * Centro de custo ao qual o lançamento está vinculado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_custo_id")
    private CentroCusto centroCusto;

    /**
     * Plano de contas contábil associado ao lançamento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_conta_id")
    private PlanoConta planoConta;
}
