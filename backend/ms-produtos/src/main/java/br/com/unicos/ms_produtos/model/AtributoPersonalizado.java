package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

/**
 * Entidade que representa um atributo configurável de produto,
 * associado a uma categoria específica dentro de uma empresa (tenant).
 *
 * <p>
 * Exemplo de atributos: Cor, Tamanho, Material, Voltagem.
 * Cada empresa possui seu próprio conjunto de atributos,
 * mesmo que compartilhem nomes semelhantes.
 * </p>
 */
@Entity
@Table(
        name = "atributo_personalizado",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_atributo_empresa_categoria",
                        columnNames = {"empresa_id", "nome", "categoria_id"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtributoPersonalizado {

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
     * Nome do atributo (ex.: "Cor", "Tamanho").
     */
    @NotBlank(message = "O nome do atributo é obrigatório.")
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Categoria à qual o atributo pertence.
     * Os atributos são específicos por categoria e por empresa.
     */
    @NotNull(message = "A categoria é obrigatória.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @ToString.Exclude
    private Categoria categoria;

    /**
     * Valores atribuídos a produtos que utilizam este atributo.
     */
    @OneToMany(
            mappedBy = "atributoPersonalizado",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ProdutoAtributoValor> valores;
}
