package br.com.unicos.ms_estoque.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.enums.TipoAtuacaoEstoque;
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
 * Representa o vínculo entre um estoque e uma filial.
 */
@Entity
@Table(
        name = "vinculo_estoque_filial",
        indexes = {
                @Index(name = "ix_vinc_est_fil_estoque_id", columnList = "estoque_id"),
                @Index(name = "ix_vinc_est_fil_filial_id", columnList = "filial_id"),
                @Index(name = "ix_vinc_est_fil_empresa_estoque", columnList = "empresa_id, estoque_id"),
                @Index(name = "ix_vinc_est_fil_empresa_filial", columnList = "empresa_id, filial_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VinculoEstoqueFilial extends BaseTenantEntity {

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
     * Identificador da filial.
     */
    @NotNull(message = "O identificador da filial é obrigatório.")
    @Column(name = "filial_id", nullable = false)
    private Long filialId;

    /**
     * Tipo de atuação do estoque na filial.
     */
    @NotNull(message = "O tipo de atuação é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atuacao", nullable = false, length = 30)
    private TipoAtuacaoEstoque tipoAtuacao;

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
     * Status do vínculo entre estoque e filial.
     */
    @NotNull(message = "O status do vínculo é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_vinculo_estoque_filial", nullable = false, length = 30)
    private StatusVinculoEstoqueFilial statusVinculoEstoqueFilial;
}