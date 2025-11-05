package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Entidade que representa o valor de um atributo personalizado para um produto específico.
 */
@Entity
@Table(name = "produto_atributo_valor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoAtributoValor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Produto ao qual o atributo pertence
     */
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * Atributo personalizado associado
     */
    @ManyToOne
    @JoinColumn(name = "atributo_personalizado_id", nullable = false)
    private AtributoPersonalizado atributoPersonalizado;

    /**
     * Valor atribuído ao produto
     */
    @NotBlank(message = "O valor do atributo é obrigatório.")
    @Column(nullable = false, length = 100)
    private String valor;
}
