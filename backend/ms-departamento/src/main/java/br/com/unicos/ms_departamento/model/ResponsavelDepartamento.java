package br.com.unicos.ms_departamento.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_departamento.enums.PapelResponsavelDepartamento;
import br.com.unicos.ms_departamento.enums.StatusResponsavelDepartamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Representa o responsável (gestor/ponto focal) por um departamento.
 * <p>
 * Integração lógica com ms-pessoas/ms-rh por meio do identificador
 * do colaborador/pessoa, sem FK física.
 */
@Entity
@Table(
        name = "responsavel_departamento",
        indexes = {
                @Index(name = "ix_resp_dep_departamento_id", columnList = "departamento_id"),
                @Index(name = "ix_resp_dep_responsavel_id", columnList = "responsavel_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ResponsavelDepartamento extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador do departamento.
     * <p>
     * No MVP, modelado por id para manter o agregado simples.
     */
    @NotNull
    @Column(name = "departamento_id", nullable = false)
    private Long departamentoId;

    /**
     * Identificador do responsável no ms-pessoas/ms-rh.
     * <p>
     * Sem FK física, pois pertence a outro bounded context.
     */
    @NotNull
    @Column(name = "responsavel_id", nullable = false)
    private Long responsavelId;

    /**
     * Papel do responsável no departamento (ex.: GESTOR, SUBGESTOR, PONTO_FOCAL).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false, length = 30)
    private PapelResponsavelDepartamento papel;

    /**
     * Define se este responsável é o principal para o departamento.
     */
    @NotNull
    @Column(name = "principal", nullable = false)
    private Boolean principal;

    /**
     * Início da vigência do responsável no departamento.
     */
    @NotNull
    @Column(name = "vigencia_inicio", nullable = false)
    private LocalDate vigenciaInicio;

    /**
     * Fim da vigência do responsável no departamento (opcional).
     */
    @Column(name = "vigencia_fim")
    private LocalDate vigenciaFim;

    /**
     * Status do vínculo do responsável com o departamento.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_responsavel_departamento", nullable = false, length = 20)
    private StatusResponsavelDepartamento statusResponsavelDepartamento;
}
