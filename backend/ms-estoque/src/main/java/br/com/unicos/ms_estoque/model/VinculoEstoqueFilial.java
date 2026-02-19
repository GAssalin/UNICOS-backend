package br.com.unicos.ms_estoque.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.enums.TipoAtuacaoEstoque;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Representa o vínculo entre um estoque e uma filial.
 * <p>
 * Esta entidade permite que um mesmo estoque (ex.: Financeiro)
 * exista em múltiplas filiais, ou atue de forma corporativa/compartilhada.
 * Integrações lógicas:
 * - estoqueId: ms-estoque
 * - filialId: ms-filial
 * <p>
 * Não possui FK física para preservar independência entre microserviços.
 */
@Entity
@Table(
        name = "vinculo_estoque_filial",
        indexes = {
                @Index(name = "ix_vinc_dep_fil_estoque_id", columnList = "estoque_id"),
                @Index(name = "ix_vinc_dep_fil_filial_id", columnList = "filial_id")
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
     * Identificador do estoque (ms-estoque).
     */
    @NotNull
    @Column(name = "estoque_id", nullable = false)
    private Long estoqueId;

    /**
     * Identificador da filial (ms-filial).
     */
    @NotNull
    @Column(name = "filial_id", nullable = false)
    private Long filialId;

    /**
     * Tipo de atuação do estoque na filial.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atuacao", nullable = false, length = 30)
    private TipoAtuacaoEstoque tipoAtuacao;

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
     * Status do vínculo Estoque x Filial.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_vinculo_estoque_filial", nullable = false, length = 20)
    private StatusVinculoEstoqueFilial statusVinculoEstoqueFilial;
}
