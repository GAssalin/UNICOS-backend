package br.com.unicos.ms_produtos.model;

import br.com.unicos.core.produto.model.PrecoBase;
import br.com.unicos.core.produto.model.ProdutoBase;
import br.com.unicos.core.produto.model.ProdutoTributacaoBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

/**
 * Entidade que representa o produto cadastrado dentro do contexto operacional
 * de uma empresa (tenant) no ERP UniCoS.
 *
 * <p>
 * O módulo core-produto define o conceito universal do produto,
 * enquanto o ms-produtos armazena dados operacionais e relacionais
 * específicos de cada empresa.
 * </p>
 */
@Entity
@Table(
        name = "produto",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_produto_empresa_sku",
                        columnNames = {"empresa_id", "dados_basicos_sku"}
                )
        },
        indexes = {
                @Index(name = "idx_produto_empresa", columnList = "empresa_id"),
                @Index(name = "idx_produto_empresa_categoria", columnList = "empresa_id, categoria_id"),
                @Index(name = "idx_produto_empresa_marca", columnList = "empresa_id, marca_id")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    // ============================================================
    // 🔹 Identificador
    // ============================================================

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

    // ============================================================
    // 🔹 Dados universais (core-produto)
    // ============================================================

    /**
     * Dados básicos como nome, descrição, SKU, classificação e tipos.
     * O SKU é único apenas dentro do contexto da empresa.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "nome", column = @Column(name = "dados_basicos_nome")),
            @AttributeOverride(name = "descricao", column = @Column(name = "dados_basicos_descricao")),
            @AttributeOverride(name = "sku", column = @Column(name = "dados_basicos_sku")),
            @AttributeOverride(name = "tipoProduto", column = @Column(name = "dados_basicos_tipo")),
            @AttributeOverride(name = "tipoVariacaoProduto", column = @Column(name = "dados_basicos_variacao")),
            @AttributeOverride(name = "tipoOrigemProduto", column = @Column(name = "dados_basicos_origem")),
            @AttributeOverride(name = "tipoControleEstoque", column = @Column(name = "dados_basicos_controle_estoque")),
            @AttributeOverride(name = "tipoArmazenamentoProduto", column = @Column(name = "dados_basicos_armazenamento")),
            @AttributeOverride(name = "tipoClassificacaoProduto", column = @Column(name = "dados_basicos_classificacao")),
            @AttributeOverride(name = "statusProduto", column = @Column(name = "dados_basicos_status"))
    })
    private ProdutoBase dadosBasicos;

    /**
     * Informações tributárias do produto.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "ncm", column = @Column(name = "tributacao_ncm")),
            @AttributeOverride(name = "cest", column = @Column(name = "tributacao_cest")),
            @AttributeOverride(name = "situacaoTributaria", column = @Column(name = "tributacao_situacao"))
    })
    private ProdutoTributacaoBase tributacao;

    /**
     * Preço atual do produto.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "precoCusto", column = @Column(name = "preco_custo")),
            @AttributeOverride(name = "precoVenda", column = @Column(name = "preco_venda")),
            @AttributeOverride(name = "precoMinimo", column = @Column(name = "preco_minimo")),
            @AttributeOverride(name = "margemPadrao", column = @Column(name = "margem_padrao"))
    })
    private PrecoBase precoAtual;

    // ============================================================
    // 🔹 Campos operacionais
    // ============================================================

    /**
     * Produto ativo no ERP.
     */
    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    /**
     * Categoria organizacional da empresa.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @ToString.Exclude
    private Categoria categoria;

    /**
     * Marca cadastrada no ERP.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    @ToString.Exclude
    private Marca marca;

    // ============================================================
    // 🔹 Relacionamentos específicos
    // ============================================================

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ImagemProduto> imagens;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ProdutoAtributoValor> atributos;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FornecedorProduto> fornecedores;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ProdutoVariacao> variacoes;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<HistoricoPreco> historicoPrecos;
}
