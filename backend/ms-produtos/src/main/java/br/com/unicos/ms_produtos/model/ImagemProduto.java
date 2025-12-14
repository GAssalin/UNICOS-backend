package br.com.unicos.ms_produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidade que representa uma imagem associada a um produto,
 * sempre no contexto de uma empresa (tenant).
 *
 * <p>
 * Permite múltiplas imagens por produto, definição de imagem principal,
 * ordenação de exibição e metadados para acessibilidade e SEO.
 * </p>
 */
@Entity
@Table(
        name = "imagem_produto",
        indexes = {
                @Index(
                        name = "idx_imagem_produto_empresa_ordem",
                        columnList = "empresa_id, produto_id, ordem_exibicao"
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagemProduto {

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
     * Produto ao qual a imagem pertence.
     * O produto sempre pertence à mesma empresa.
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
    @NotNull
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
    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
