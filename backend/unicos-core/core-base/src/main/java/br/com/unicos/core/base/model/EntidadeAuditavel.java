package br.com.unicos.core.base.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * Classe base para auditoria de entidades no sistema UniCoS.
 *
 * <p>
 * Fornece campos padrão para controle de criação, atualização e exclusão lógica.
 * Todas as entidades que herdarem esta classe terão automaticamente os campos de auditoria.
 * </p>
 */
@MappedSuperclass
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
        this.criadoEm = LocalDateTime.now();
    }

}
