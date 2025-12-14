package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

/**
 * Entidade que representa uma marca ou fabricante de produtos,
 * sempre vinculada a uma empresa (tenant).
 *
 * <p>
 * Cada empresa possui seu próprio catálogo de marcas,
 * mesmo que marcas com o mesmo nome existam em outros tenants.
 * </p>
 */
@Entity
@Table(
        name = "marca",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_marca_empresa_nome",
                        columnNames = {"empresa_id", "nome"}
                )
        },
        indexes = {
                @Index(name = "idx_marca_empresa", columnList = "empresa_id"),
                @Index(name = "idx_marca_empresa_nome", columnList = "empresa_id, nome"),
                @Index(name = "idx_marca_pais", columnList = "pais_origem")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marca {

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
     * Nome da marca.
     * Único dentro do contexto da empresa.
     */
    @NotBlank(message = "O nome da marca é obrigatório.")
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Descrição resumida da marca.
     */
    @Column(length = 255)
    private String descricao;

    /**
     * País de origem do fabricante.
     */
    @Column(name = "pais_origem", length = 100)
    private String paisOrigem;

    /**
     * Lista de produtos associados à marca.
     *
     * <p>
     * Relacionamento LAZY para evitar carregamento
     * desnecessário do catálogo de produtos.
     * </p>
     */
    @OneToMany(mappedBy = "marca", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Produto> produtos;
}
