package br.com.unicos.core.tesouraria.model;

import br.com.unicos.core.tesouraria.enums.TipoLancamentoFinanceiro;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa um lançamento financeiro realizado em uma conta.
 *
 * <p>
 * O lançamento pode ser de entrada (receita) ou saída (despesa),
 * e é utilizado para controle do fluxo de caixa e conciliação bancária.
 * </p>
 */
@Entity
@Table(name = "lancamento_financeiro")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoFinanceiro {

    /**
     * Identificador único do lançamento financeiro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Conta financeira na qual o lançamento foi registrado.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_financeira_id", nullable = false)
    private ContaFinanceira contaFinanceira;

    /**
     * Tipo de lançamento (ex: Receita, Despesa, Transferência, Ajuste).
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, length = 30)
    private TipoLancamentoFinanceiro tipoLancamento;

    /**
     * Valor total do lançamento.
     */
    @NotNull
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorBruto;

    /**
     * Valor líquido após descontos, taxas ou encargos.
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal valorLiquido;

    /**
     * Descrição ou observação do lançamento.
     */
    @Column(length = 255)
    private String descricao;

    /**
     * Data em que o lançamento foi efetuado.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate dataLancamento;

    /**
     * Data de competência contábil do lançamento.
     * Utilizada para fechamento e conciliação.
     */
    @Column
    private LocalDate dataCompetencia;

    /**
     * Identificador de origem do lançamento (ex: ID da fatura, pedido ou nota).
     * Pode ser utilizado para rastrear o módulo de origem no sistema.
     */
    @Column(length = 100)
    private String referenciaOrigem;

    /**
     * Identificador do registro de origem (ex: ms-compras, ms-vendas, ms-pagamento).
     */
    @Column(length = 50)
    private String origemSistema;
}
