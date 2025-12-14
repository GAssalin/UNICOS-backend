package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidade que representa o valor associado a um atributo personalizado
 * para um produto específico, sempre no contexto de uma empresa (tenant).
 *
 * <p>
 * Exemplo: Produto X → Cor = Azul, Tamanho = G.
 * Cada empresa possui seus próprios valores de atributos,
 * mesmo que produtos e atributos sejam semelhantes entre tenants.
 * </p>
 */
@Entity
@Table(
        name = "produto_atributo_valor",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_produto_atributo_empresa",
                        columnNames = {"empresa_id", "produto_id", "atributo_personalizado_id"}
                )
        },
        indexes = {
                @Index(name = "idx_prod_attr_empresa", columnList = "empresa_id"),
                @Index(name = "idx_prod_attr_empresa_produto", columnList = "empresa_id, produto_id")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoAtributoValor {

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
     * Produto ao qual o valor de atributo está associado.
     * O produto sempre pertence à mesma empresa.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * Atributo personalizado definido no catálogo da empresa.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atributo_personalizado_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AtributoPersonalizado atributoPersonalizado;

    /**
     * Valor atribuído ao produto (ex.: "Azul", "G", "500ml").
     */
    @NotBlank(message = "O valor do atributo é obrigatório.")
    @Column(nullable = false, length = 100)
    private String valor;
}
