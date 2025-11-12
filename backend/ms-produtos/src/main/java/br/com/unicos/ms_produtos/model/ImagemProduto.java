package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entidade que representa uma imagem associada a um produto.
 * Pode armazenar múltiplas imagens para um mesmo produto,
 * definindo qual é a principal e sua ordem de exibição.
 */
@Entity
@Table(name = "imagem_produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagemProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Produto ao qual a imagem pertence
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * URL ou caminho da imagem
     */
    @NotBlank(message = "A URL da imagem é obrigatória.")
    @Column(nullable = false, length = 500)
    private String url;

    /**
     * Texto alternativo para acessibilidade e SEO
     */
    @Column(name = "descricao_alt", length = 255)
    private String descricaoAlt;

    /**
     * Indica se é a imagem principal do produto
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean principal = false;

    /**
     * Define a ordem de exibição da imagem (1 = primeira, 2 = segunda, etc.)
     */
    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao;

    /**
     * Status da imagem (ativa/inativa)
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
