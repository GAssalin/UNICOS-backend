package br.com.unicos.core.tesouraria.model;

import br.com.unicos.core.tesouraria.enums.TipoContaFinanceira;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entidade que representa uma conta financeira da empresa.
 * <p>
 * Pode ser uma conta bancária, caixa físico ou conta de investimento.
 */
@Entity
@Table(name = "conta_financeira")
@Getter
@Setter
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
     * Nome de identificação da conta (ex.: Caixa Principal, Banco Itaú).
     */
    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Tipo da conta financeira (corrente, poupança, caixa físico, etc.).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoContaFinanceira tipo;

    /**
     * Código da agência bancária (quando aplicável).
     */
    @Column(length = 30)
    private String agencia;

    /**
     * Número da conta bancária (quando aplicável).
     */
    @Column(length = 30)
    private String numeroConta;

    /**
     * Nome do banco ou instituição financeira.
     */
    @Column(length = 50)
    private String banco;

    /**
     * Indica se a conta está ativa para movimentações.
     */
    @NotNull
    @Column(nullable = false)
    private Boolean ativo;
}
