package br.com.unicos.core.financeiro.model;

import br.com.unicos.core.financeiro.enums.TipoContaFinanceira;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma conta financeira utilizada pela empresa.
 * <p>
 * Pode corresponder a contas bancárias, caixas físicos ou carteiras digitais.
 * É a base para movimentações financeiras em módulos como Tesouraria,
 * Contas a Pagar e Contas a Receber.
 */
@Entity
@Table(name = "conta_financeira")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaFinanceira {

    /**
     * Identificador único da conta financeira.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome descritivo da conta (ex: Caixa Matriz, Banco Itaú, Carteira Digital).
     */
    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Tipo da conta financeira (ex: conta corrente, caixa, investimento).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoContaFinanceira tipo;

    /**
     * Nome do banco ao qual a conta pertence (opcional para caixas físicos).
     */
    @Column(length = 100)
    private String banco;

    /**
     * Código da agência bancária.
     */
    @Column(length = 20)
    private String agencia;

    /**
     * Número da conta bancária.
     */
    @Column(length = 30)
    private String numeroConta;

    /**
     * Saldo atual da conta.
     */
    @Column(nullable = false)
    private Double saldoAtual;

    /**
     * Indica se a conta está ativa para uso.
     */
    @Column(nullable = false)
    private Boolean ativo;

}
