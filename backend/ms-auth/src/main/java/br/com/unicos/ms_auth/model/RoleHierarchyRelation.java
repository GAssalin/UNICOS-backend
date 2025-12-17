package br.com.unicos.ms_auth.model;

import br.com.unicos.core.base.model.EntidadeAuditavel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entidade responsável por representar a relação hierárquica
 * entre dois papéis do sistema.
 * <p>
 * Cada registro define uma relação no formato:
 * <p>
 * parentRole > childRole
 * <p>
 * Essa estrutura permite que a hierarquia de permissões seja
 * carregada dinamicamente, sem a necessidade de alterar o código
 * a cada inclusão ou modificação de roles.
 */
@Entity
@Table(
        name = "role_hierarchy_relation",
        indexes = {
                @Index(name = "idx_parent_role", columnList = "parent_role"),
                @Index(name = "idx_child_role", columnList = "child_role")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_parent_child_role",
                        columnNames = {"parent_role", "child_role"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RoleHierarchyRelation extends EntidadeAuditavel {

    /**
     * Identificador único da relação de hierarquia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Papel que está acima na hierarquia.
     * Exemplo: ROLE_PRODUTO_ADMIN
     */
    @NotBlank
    @Column(name = "parent_role", nullable = false, length = 100)
    private String parentRole;

    /**
     * Papel que herda as permissões do papel pai.
     * Exemplo: ROLE_PRODUTO_GERENTE
     */
    @NotBlank
    @Column(name = "child_role", nullable = false, length = 100)
    private String childRole;
}
