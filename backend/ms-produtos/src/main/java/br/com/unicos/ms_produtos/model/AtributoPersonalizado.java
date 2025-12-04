package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que representa um atributo configurável de produto,
 * geralmente associado a uma categoria (ex.: "Cor", "Tamanho").
 *
 * <p>
 * Os atributos personalizados permitem que produtos de uma mesma categoria
 * compartilhem características específicas definidas pela empresa.
 * </p>
 */
@Entity
@Table(
        name = "atributo_personalizado",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_atributo_categoria",
                        columnNames = {"nome", "categoria_id"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtributoPersonalizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do atributo (ex.: "Cor", "Tamanho").
     */
    @NotBlank(message = "O nome do atributo é obrigatório.")
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Categoria à qual o atributo pertence.
     * Cada categoria possui seu conjunto próprio de atributos.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @ToString.Exclude
    private Categoria categoria;

    /**
     * Lista de valores de atributo aplicados a produtos associados a este atributo.
     */
    @OneToMany(mappedBy = "atributoPersonalizado", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ProdutoAtributoValor> valores;
}
