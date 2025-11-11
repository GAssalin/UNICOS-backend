package br.com.unicos.core.financeiro.model;

import br.com.unicos.core.financeiro.enums.Periodicidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa as configurações financeiras padrão do sistema.
 * <p>
 * Armazena parâmetros utilizados para cálculos e regras automáticas,
 * como juros, multa e periodicidade de lançamentos.
 */
@Entity
@Table(name = "configuracao_financeira")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracaoFinanceira {

    /**
     * Identificador único das configurações financeiras.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Percentual de juros padrão aplicado em lançamentos em atraso.
     */
    @NotNull
    @Column(nullable = false)
    private Double jurosPadrao;

    /**
     * Percentual de multa padrão aplicado em casos de atraso.
     */
    @NotNull
    @Column(nullable = false)
    private Double multaPadrao;

    /**
     * Periodicidade padrão para lançamentos recorrentes.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Periodicidade periodicidade;

    /**
     * Indica se esta configuração está ativa no sistema.
     */
    @Column(nullable = false)
    private Boolean ativo;
}
