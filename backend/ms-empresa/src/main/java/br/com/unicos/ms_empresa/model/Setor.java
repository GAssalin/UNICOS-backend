package br.com.unicos.ms_empresa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa a subdivisão de um departamento dentro da empresa.
 * <p>
 * Exemplo: Departamento Financeiro → Setor de Contas a Pagar.
 */
@Entity
@Table(name = "setor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
