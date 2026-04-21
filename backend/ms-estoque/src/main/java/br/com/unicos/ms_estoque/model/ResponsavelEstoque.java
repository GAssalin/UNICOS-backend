package br.com.unicos.ms_estoque.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_estoque.enums.PapelResponsavelEstoque;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Representa o responsável por um estoque.
 *
 * <p>Integração lógica com outros microserviços por identificadores, sem FK física.</p>
 */
@Entity
@Table(
        name = "responsavel_estoque",
        indexes = {
                @Index(name = "ix_resp_est_estoque_id", columnList = "estoque_id"),
                @Index(name = "ix_resp_est_responsavel_id", columnList = "responsavel_id"),
                @Index(name = "ix_resp_est_empresa_estoque", columnList = "empresa_id, estoque_id")
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
     */
    @NotNull(message = "O identificador do estoque é obrigatório.")
    @Column(name = "estoque_id", nullable = false)
    private Long estoqueId;

    /**
     * Identificador do responsável em outro microserviço.
     */
    @NotNull(message = "O identificador do responsável é obrigatório.")
    @Column(name = "responsavel_id", nullable = false)
    private Long responsavelId;

    /**
     * Papel do responsável no estoque.
     */
    @NotNull(message = "O papel do responsável é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false, length = 30)
    private PapelResponsavelEstoque papel;

    /**
     * Indica se o responsável é o principal.
     */
    @NotNull(message = "A informação de principal é obrigatória.")
    @Column(name = "principal", nullable = false)
    private Boolean principal;

    /**
     * Início da vigência do vínculo.
     */
    @NotNull(message = "A vigência inicial é obrigatória.")
    @Column(name = "vigencia_inicio", nullable = false)
    private LocalDate vigenciaInicio;

    /**
     * Fim da vigência do vínculo.
     */
    @Column(name = "vigencia_fim")
    private LocalDate vigenciaFim;

    /**
     * Status do vínculo do responsável com o estoque.
     */
    @NotNull(message = "O status do responsável do estoque é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_responsavel_estoque", nullable = false, length = 20)
    private StatusResponsavelEstoque statusResponsavelEstoque;
}