package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

/**
 * Entidade que representa um atributo configurável de produto,
 * normalmente associado a uma categoria (ex: "Cor", "Tamanho").
 */
@Entity
@Table(name = "atributo_personalizado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtributoPersonalizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do atributo (ex: Cor, Tamanho)
     */
    @NotBlank(message = "O nome do atributo é obrigatório.")
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Categoria à qual o atributo pertence
     */
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    /**
     * Lista de valores de atributo aplicados a produtos
     */
    @OneToMany(mappedBy = "atributoPersonalizado", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ProdutoAtributoValor> valores;
}
