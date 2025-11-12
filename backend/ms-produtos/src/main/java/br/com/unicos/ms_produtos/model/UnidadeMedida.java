package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma unidade de medida (ex: KG, UN, CX).
 * Utilizada para padronização e associação com produtos.
 */
@Entity
@Table(name = "unidade_medida")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnidadeMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome completo da unidade (ex: Quilograma, Unidade, Caixa)
     */
    @NotBlank(message = "O nome da unidade é obrigatório.")
    @Column(nullable = false, length = 50)
    private String nome;

    /**
     * Sigla da unidade (ex: KG, UN, CX)
     */
    @NotBlank(message = "A sigla é obrigatória.")
    @Column(nullable = false, length = 10, unique = true)
    private String sigla;

    /**
     * Descrição opcional da unidade
     */
    @Column(length = 255)
    private String descricao;

    /**
     * Status da unidade de medida
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
