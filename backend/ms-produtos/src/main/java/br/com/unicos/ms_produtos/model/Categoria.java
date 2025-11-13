package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que representa uma categoria de produtos.
 * Permite organização hierárquica e agrupamento de itens no catálogo.
 */
@Entity
@Table(
        name = "categoria",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_categoria_nome_pai",
                        columnNames = {"nome", "categoria_pai_id"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private String descricao;

    /**
     * Categoria pai no modelo hierárquico.
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
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
