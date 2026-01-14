package br.com.unicos.ms_filial.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import br.com.unicos.ms_filial.enums.StatusFilial;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Representa o histórico de mudanças de status de uma filial dentro do UniCoS.
 * <p>
 * No MVP do ms-filial, esta entidade registra transições de status para auditoria
 * e rastreabilidade (ex.: ATIVA -> INATIVA), incluindo motivo e responsável.
 */
@Entity
@Table(name = "filial_status_historico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FilialStatusHistorico extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador da filial relacionada ao histórico.
     */
    @NotNull
    @Column(name = "filial_id", nullable = false)
    private Long filialId;

    /**
     * Status anterior da filial.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", length = 20)
    private StatusFilial statusAnterior;

    /**
     * Novo status da filial após a alteração.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false, length = 20)
    private StatusFilial statusNovo;

    /**
     * Data e hora da alteração do status.
     */
    @NotNull
    @Column(name = "data_alteracao", nullable = false)
    private LocalDateTime dataAlteracao;

    /**
     * Motivo/observação da mudança de status.
     */
    @NotBlank
    @Column(name = "motivo", nullable = false, length = 300)
    private String motivo;

    /**
     * Identificador do usuário responsável pela alteração.
     * Integração lógica com ms-auth/ms-pessoas, sem FK física.
     */
    @Column(name = "usuario_id")
    private Long usuarioId;
}
