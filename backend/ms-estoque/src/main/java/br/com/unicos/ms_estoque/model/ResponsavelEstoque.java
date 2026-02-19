package br.com.unicos.ms_estoque.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_estoque.enums.PapelResponsavelEstoque;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Representa o responsável (gestor/ponto focal) por um estoque.
 * <p>
 * Integração lógica com ms-pessoas/ms-rh por meio do identificador
 * do colaborador/pessoa, sem FK física.
 */
@Entity
@Table(
        name = "responsavel_estoque",
        indexes = {
                @Index(name = "ix_resp_dep_estoque_id", columnList = "estoque_id"),
                @Index(name = "ix_resp_dep_responsavel_id", columnList = "responsavel_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ResponsavelEstoque extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador do estoque.
     * <p>
     * No MVP, modelado por id para manter o agregado simples.
     */
    @NotNull
    @Column(name = "estoque_id", nullable = false)
    private Long estoqueId;

    /**
     * Identificador do responsável no ms-pessoas/ms-rh.
     * <p>
     * Sem FK física, pois pertence a outro bounded context.
     */
    @NotNull
    @Column(name = "responsavel_id", nullable = false)
    private Long responsavelId;

    /**
     * Papel do responsável no estoque (ex.: GESTOR, SUBGESTOR, PONTO_FOCAL).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false, length = 30)
    private PapelResponsavelEstoque papel;

    /**
     * Define se este responsável é o principal para o estoque.
     */
    @NotNull
    @Column(name = "principal", nullable = false)
    private Boolean principal;

    /**
     * Início da vigência do responsável no estoque.
     */
    @NotNull
    @Column(name = "vigencia_inicio", nullable = false)
    private LocalDate vigenciaInicio;

    /**
     * Fim da vigência do responsável no estoque (opcional).
     */
    @Column(name = "vigencia_fim")
    private LocalDate vigenciaFim;

    /**
     * Status do vínculo do responsável com o estoque.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_responsavel_estoque", nullable = false, length = 20)
    private StatusResponsavelEstoque statusResponsavelEstoque;
}
