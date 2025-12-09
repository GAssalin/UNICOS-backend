package br.com.unicos.ms_produtos.model;

import br.com.unicos.core.produto.model.PrecoBase;
import br.com.unicos.core.produto.model.ProdutoBase;
import br.com.unicos.core.produto.model.ProdutoTributacaoBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

/**
 * Entidade que representa o produto cadastrado dentro do contexto operacional
 * da empresa (ERP UniCoS).
 *
 * <p>
 * Enquanto o módulo <b>core-produto</b> define o conceito universal e imutável
 * do produto, o <b>ms-produtos</b> armazena os dados complementares e
 * operacionais, como atributos personalizados, imagens, fornecedores,
 * categorias, variações e histórico de preços.
 * </p>
 *
 * <p>
 * Esta entidade utiliza composição com modelos base do core-produto
 * (via {@link Embedded}), garantindo padronização entre microserviços
 * sem duplicação de lógica.
 * </p>
 */
@Entity
@Table(name = "produto")
@EntityListeners(AuditingEntityListener.class)
@Data
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

    // ============================================================
    // 🔹 Dados universais (core-produto)
    // ============================================================

    /**
     * Dados básicos como nome, descrição, SKU global, classificação e tipos.
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
     * Informações tributárias universais do produto.
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
    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;

    /**
     * Categoria organizacional da empresa.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    /**
     * Marca cadastrada no ERP.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    // ============================================================
    // 🔹 Relacionamentos específicos
    // ============================================================

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagemProduto> imagens;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoAtributoValor> atributos;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FornecedorProduto> fornecedores;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoVariacao> variacoes;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoPreco> historicoPrecos;

}
