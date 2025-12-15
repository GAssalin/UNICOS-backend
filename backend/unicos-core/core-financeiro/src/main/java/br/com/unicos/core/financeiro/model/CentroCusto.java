package br.com.unicos.core.financeiro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um centro de custo.
 * <p>
 * Utilizada para agrupar e classificar lançamentos financeiros
 * por área, projeto ou departamento da empresa.
 */
@Entity
@Table(name = "centro_custo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CentroCusto {

    /**
     * Identificador único do centro de custo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código de identificação do centro de custo.
     */
    @NotBlank
    @Column(nullable = false, length = 20, unique = true)
    private String codigo;

    /**
     * Nome ou título do centro de custo.
     */
    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Descrição detalhada da finalidade do centro de custo.
     */
    @Column(length = 255)
    private String descricao;

    /**
     * Indica se o centro de custo está ativo.
     */
    @Column(nullable = false)
    private Boolean ativo;
}
