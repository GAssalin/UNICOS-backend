package br.com.unicos.core.tesouraria.model;

import br.com.unicos.core.tesouraria.enums.MeioPagamento;
import br.com.unicos.core.tesouraria.enums.TipoLancamentoFinanceiro;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa um lançamento financeiro.
 * <p>
 * Registra movimentações diretas de entrada ou saída de valores
 * em uma conta financeira, com detalhamento do meio de pagamento.
 */
@Entity
@Table(name = "lancamento_financeiro")
@Getter
@Setter
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
     * Conta financeira associada ao lançamento.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_financeira_id", nullable = false)
    private ContaFinanceira conta;

    /**
     * Tipo do lançamento (entrada, saída ou transferência).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoLancamentoFinanceiro tipo;

    /**
     * Meio de pagamento utilizado na movimentação.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MeioPagamento meioPagamento;

    /**
     * Valor da movimentação financeira.
     */
    @NotNull
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    /**
     * Data de realização do lançamento.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate data;

    /**
     * Descrição ou observação adicional sobre o lançamento.
     */
    @Column(length = 255)
    private String descricao;
}
