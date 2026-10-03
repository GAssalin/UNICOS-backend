package br.com.unicos.ms_permissao.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Define os papéis (grupos de permissões) atribuíveis aos usuários.
 */
@Entity
@Table(
        name = "role",
        uniqueConstraints = @UniqueConstraint(name = "uk_role_empresa_nome", columnNames = {"empresa_id", "nome"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Role extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String nome;

    @Column(length = 255)
    private String descricao;

}