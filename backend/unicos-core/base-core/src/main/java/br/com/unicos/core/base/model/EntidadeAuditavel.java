package br.com.unicos.core.base.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
public abstract class EntidadeAuditavel {

    /**
     * Identificador do usuário responsável pela criação do registro.
     */
    @Column(name = "criado_por")
    private Long criadoPor;

    /**
     * Data e hora de criação do registro.
     */
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    /**
     * Identificador do usuário responsável pela última atualização do registro.
     */
    @Column(name = "atualizado_por")
    private Long atualizadoPor;

    /**
     * Data e hora da última atualização do registro.
     */
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    /**
     * Indica se o registro foi removido logicamente do sistema.
     */
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
}
