package br.com.unicos.ms_departamento.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_departamento.enums.StatusDepartamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa um departamento (unidade organizacional) dentro do UniCoS.
 * <p>
 * É a entidade central do microserviço ms-departamento, responsável por
 * identificar a área organizacional (ex.: Financeiro, Comercial, RH),
 * manter seu status e permitir integrações com outros microserviços
 * (ex.: ms-filial, ms-rh, ms-pessoas) através de identificadores lógicos.
 */
@Entity
@Table(
        name = "departamento",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_departamento_codigo", columnNames = {"codigo"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Departamento extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código interno do departamento (ex.: "DEP-FIN", "DEP-RH").
     */
    @NotBlank
    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    /**
     * Nome do departamento (ex.: "Financeiro", "Recursos Humanos").
     */
    @NotBlank
    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    /**
     * Descrição livre do departamento (opcional).
     */
    @Column(name = "descricao", length = 500)
    private String descricao;

    /**
     * Status do departamento.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_departamento", nullable = false, length = 20)
    private StatusDepartamento statusDepartamento;

    /**
     * Identificador do departamento pai (auto-relacionamento lógico).
     * <p>
     * Mantido como id para simplificar o agregado no MVP.
     */
    @Column(name = "departamento_pai_id")
    private Long departamentoPaiId;
}
