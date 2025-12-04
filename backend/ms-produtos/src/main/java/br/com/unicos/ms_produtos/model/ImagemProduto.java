package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidade que representa uma imagem associada a um produto.
 *
 * <p>
 * Suporta múltiplas imagens por produto, permitindo definir qual é a principal,
 * a ordem de exibição e metadados úteis como texto alternativo.
 * </p>
 */
@Entity
@Table(
        name = "imagem_produto",
        indexes = {
                @Index(name = "idx_imagem_produto_ordem", columnList = "produto_id, ordem_exibicao")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagemProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Produto ao qual a imagem pertence.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produto produto;

    /**
     * URL ou caminho da imagem armazenada (ex.: S3, CDN).
     */
    @NotBlank(message = "A URL da imagem é obrigatória.")
    @Column(nullable = false, length = 500)
    private String url;

    /**
     * Texto alternativo para acessibilidade e SEO.
     */
    @Column(name = "descricao_alt", length = 255)
    private String descricaoAlt;

    /**
     * Indica se esta é a imagem principal do produto.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean principal = false;

    /**
     * Ordem de exibição da imagem (1 = primeira, 2 = segunda...).
     */
    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao;

    /**
     * Status da imagem (ativa ou não).
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
