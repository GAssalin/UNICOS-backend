package br.com.unicos.core.tenant.model;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entidade base para modelos multi-tenant.
 *
 * <p>
 * Padroniza o campo {@code empresaId} nas entidades de negócio
 * que precisam de isolamento por tenant.
 * </p>
 *
 * <p>
 * Recomendações:
 * <ul>
 *     <li>Preencher {@code empresaId} no service ao criar entidades (a partir do TenantContext).</li>
 *     <li>Evitar {@code updatable = true} para impedir troca de tenant após persistência.</li>
 * </ul>
 * </p>
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseTenantEntity extends EntidadeAuditavel {

    /**
     * Identificador da empresa (tenant).
     * Campo obrigatório para isolamento multi-tenant.
     */
    @NotNull(message = "O identificador da empresa (tenant) é obrigatório.")
    @Column(name = "empresa_id", nullable = false, updatable = false)
    private Long empresaId;

}
