package br.com.unicos.core.base.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Classe base para auditoria das entidades do sistema.
 *
 * <p>Fornece os campos padrão de criação, atualização e exclusão lógica.</p>
 *
 * <p>
 * Os campos {@code criadoPor} e {@code atualizadoPor} são preenchidos pelo
 * {@link AuditingEntityListener}, que depende de {@code @EnableJpaAuditing}
 * e de um {@code AuditorAware} configurados no microserviço.
 * </p>
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class EntidadeAuditavel {

    @CreatedBy
    @Column(name = "criado_por", updatable = false)
    private Long criadoPor;

    @CreatedDate
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @LastModifiedBy
    @Column(name = "atualizado_por")
    private Long atualizadoPor;

    @LastModifiedDate
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @PrePersist
    public void prePersist() {
        if (this.criadoEm == null) {
            this.criadoEm = LocalDateTime.now();
        }

        if (this.ativo == null) {
            this.ativo = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}
