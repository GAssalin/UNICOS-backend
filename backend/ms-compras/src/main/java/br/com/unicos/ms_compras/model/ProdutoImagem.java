package br.com.unicos.ms_compras.model;

import br.com.unicos.core.tenant.model.BaseTenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Representa uma imagem associada a um produto dentro do catálogo UniCoS.
 *
 * <p>
 * Armazena referências (URLs/chaves) para imagens do produto, permitindo
 * que o front-end exiba uma galeria e defina uma imagem principal.
 * </p>
 *
 * <p>
 * Recomenda-se armazenar apenas a referência (URL ou chave de storage),
 * evitando salvar binários diretamente no banco.
 * </p>
 */
@Entity
@Table(name = "produto_imagem",
        indexes = {
                @Index(name = "idx_produto_imagem_empresa_produto", columnList = "empresa_id, produto_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProdutoImagem extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador do produto.
     * Integração lógica, sem FK física obrigatória.
     */
    @NotNull
    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    /**
     * Referência da imagem (URL pública ou chave no storage).
     */
    @NotBlank
    @Column(name = "url", nullable = false, length = 500)
    private String url;

    /**
     * Texto alternativo (acessibilidade / SEO).
     */
    @Column(name = "alt_texto", length = 200)
    private String altTexto;

    /**
     * Define se esta imagem é a principal do produto.
     */
    @NotNull
    @Column(name = "principal", nullable = false)
    private Boolean principal;

    /**
     * Ordem de exibição na galeria do produto.
     */
    @NotNull
    @Column(name = "ordem", nullable = false)
    private Integer ordem;

}
