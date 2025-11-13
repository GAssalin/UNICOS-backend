package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidade que representa o valor associado a um atributo personalizado
 * para um produto específico.
 *
 * <p>
 * Este modelo permite atribuir características dinâmicas ao produto,
 * como "Cor = Azul" ou "Tamanho = G".
 * </p>
 */
@Entity
@Table(
        name = "produto_atributo_valor",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_produto_atributo",
                columnNames = {"produto_id", "atributo_personalizado_id"}
        )
)
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoAtributoValor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Produto ao qual o valor de atributo está associado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * Atributo personalizado definido no catálogo.
     */
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
