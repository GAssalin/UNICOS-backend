package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

/**
 * Entidade que representa uma categoria de produtos.
 *
 * <p>
 * As categorias são organizadas de forma hierárquica (pai/filho)
 * e são sempre vinculadas a uma empresa (tenant), garantindo
 * isolamento total em ambiente multi-tenant.
 * </p>
 */
@Entity
@Table(
        name = "categoria",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_categoria_empresa_nome_pai",
                        columnNames = {"empresa_id", "nome", "categoria_pai_id"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador da empresa (tenant).
     * Campo obrigatório para isolamento multi-tenant.
     */
    @NotNull(message = "O identificador da empresa é obrigatório.")
    @Column(name = "empresa_id", nullable = false, updatable = false)
    private Long empresaId;

    /**
     * Nome da categoria.
     */
    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Descrição opcional da categoria.
     */
    @Size(max = 255)
    @Column(length = 255)
    private String descricao;

    /**
     * Categoria pai no modelo hierárquico.
     * O vínculo é sempre interno à mesma empresa.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_pai_id")
    @ToString.Exclude
    private Categoria categoriaPai;

    /**
     * Subcategorias vinculadas a esta categoria.
     */
    @OneToMany(mappedBy = "categoriaPai", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Categoria> subcategorias;

    /**
     * Status de exibição/uso da categoria.
     */
    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
