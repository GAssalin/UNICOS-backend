package br.com.unicos.ms_departamento.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_departamento.enums.StatusVinculoDepartamentoFilial;
import br.com.unicos.ms_departamento.enums.TipoAtuacaoDepartamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Representa o vínculo entre um departamento e uma filial.
 * <p>
 * Esta entidade permite que um mesmo departamento (ex.: Financeiro)
 * exista em múltiplas filiais, ou atue de forma corporativa/compartilhada.
 * Integrações lógicas:
 * - departamentoId: ms-departamento
 * - filialId: ms-filial
 * <p>
 * Não possui FK física para preservar independência entre microserviços.
 */
@Entity
@Table(
        name = "vinculo_departamento_filial",
        indexes = {
                @Index(name = "ix_vinc_dep_fil_departamento_id", columnList = "departamento_id"),
                @Index(name = "ix_vinc_dep_fil_filial_id", columnList = "filial_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VinculoDepartamentoFilial extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador do departamento (ms-departamento).
     */
    @NotNull
    @Column(name = "departamento_id", nullable = false)
    private Long departamentoId;

    /**
     * Identificador da filial (ms-filial).
     */
    @NotNull
    @Column(name = "filial_id", nullable = false)
    private Long filialId;

    /**
     * Tipo de atuação do departamento na filial.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atuacao", nullable = false, length = 30)
    private TipoAtuacaoDepartamento tipoAtuacao;

    /**
     * Início da vigência do vínculo.
     */
    @NotNull
    @Column(name = "vigencia_inicio", nullable = false)
    private LocalDate vigenciaInicio;

    /**
     * Fim da vigência do vínculo (opcional).
     */
    @Column(name = "vigencia_fim")
    private LocalDate vigenciaFim;

    /**
     * Status do vínculo Departamento x Filial.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_vinculo_departamento_filial", nullable = false, length = 20)
    private StatusVinculoDepartamentoFilial statusVinculoDepartamentoFilial;
}
