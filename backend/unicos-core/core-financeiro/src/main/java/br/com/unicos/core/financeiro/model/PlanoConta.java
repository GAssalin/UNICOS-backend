package br.com.unicos.core.financeiro.model;

import br.com.unicos.core.financeiro.enums.NaturezaFinanceira;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa o plano de contas contábil-financeiro.
 * <p>
 * Define a estrutura hierárquica das contas utilizadas
 * para classificar receitas e despesas da empresa.
 */
@Entity
@Table(name = "plano_conta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanoConta {

    /**
     * Identificador único do plano de conta.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código contábil da conta no plano (ex: 1.1.2.03).
     */
    @NotBlank
    @Column(nullable = false, length = 30, unique = true)
    private String codigo;

    /**
     * Descrição da conta contábil.
     */
    @NotBlank
    @Column(nullable = false, length = 150)
    private String descricao;

    /**
     * Natureza da conta (operacional, administrativa, investimento...).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NaturezaFinanceira natureza;

    /**
     * Conta pai, caso esta conta faça parte de uma hierarquia contábil.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_pai_id")
    private PlanoConta contaPai;

    /**
     * Indica se a conta está ativa para uso.
     */
    @Column(nullable = false)
    private Boolean ativo;
}
