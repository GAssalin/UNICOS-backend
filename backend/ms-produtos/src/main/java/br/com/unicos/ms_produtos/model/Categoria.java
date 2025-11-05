package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * Entidade que representa uma categoria de produtos.
 * Permite organização hierárquica e associação de múltiplos produtos.
 */
@Entity
@Table(name = "categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da categoria
     */
    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Descrição opcional da categoria
     */
    @Size(max = 255)
    private String descricao;

    /**
     * Categoria pai (auto-relacionamento hierárquico)
     */
    @ManyToOne
    @JoinColumn(name = "categoria_pai_id")
    private Categoria categoriaPai;

    /**
     * Subcategorias (auto-relacionamento reverso)
     */
    @OneToMany(mappedBy = "categoriaPai")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Categoria> subcategorias;

    /**
     * Lista de produtos associados à categoria
     */
    @OneToMany(mappedBy = "categoria")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Produto> produtos;

    /**
     * Status da categoria
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
