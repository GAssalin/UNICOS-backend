package br.com.unicos.core.tesouraria.model;

import br.com.unicos.core.tesouraria.enums.MeioPagamento;
import br.com.unicos.core.tesouraria.enums.TipoContaFinanceira;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa uma conta financeira utilizada pela empresa.
 *
 * <p>
 * Pode corresponder a contas bancárias (corrente, poupança, aplicação)
 * ou contas internas de caixa. É o ponto de controle dos saldos
 * e movimentações financeiras da tesouraria.
 * </p>
 */
@Entity
@Table(name = "conta_financeira")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaFinanceira {

    /**
     * Identificador único da conta financeira.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome identificador da conta (ex: Caixa Matriz, Banco Itaú, Conta Poupança).
     */
    @NotBlank
    @Column(nullable = false, length = 100)
    private String nomeConta;

    /**
     * Tipo da conta financeira (ex: Corrente, Poupança, Aplicação, Caixa Interno).
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, length = 30)
    private TipoContaFinanceira tipoConta;

    /**
     * Banco associado à conta (quando aplicável).
     */
    @Column(length = 50)
    private String banco;

    /**
     * Agência bancária da conta (quando aplicável).
     */
    @Column(length = 10)
    private String agencia;

    /**
     * Número da conta bancária (quando aplicável).
     */
    @Column(length = 20)
    private String numeroConta;

    /**
     * Meio de pagamento padrão utilizado pela conta.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private MeioPagamento meioPagamentoPadrao;

    /**
     * Saldo atual da conta.
     */
    @NotNull
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoAtual;

    /**
     * Data da última atualização do saldo.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate dataAtualizacaoSaldo;

    /**
     * Identificador da empresa proprietária da conta (referência externa).
     * Este campo deve se relacionar ao ms-empresa.
     */
    @NotNull
    @Column(nullable = false)
    private Long empresaId;

    /**
     * Identificador da filial associada à conta (referência externa).
     * Este campo deve se relacionar ao ms-empresa.
     */
    @Column
    private Long filialId;
}
