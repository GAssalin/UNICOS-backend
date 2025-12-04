package br.com.unicos.ms_produtos.model;

import br.com.unicos.core.produto.model.PrecoBase;
import br.com.unicos.core.produto.model.ProdutoBase;
import br.com.unicos.core.produto.model.ProdutoEstoqueBase;
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
    private ProdutoBase dadosBasicos;

    /**
     * Informações tributárias universais do produto.
     */
    @Embedded
    private ProdutoTributacaoBase tributacao;

    /**
     * Configurações globais de estoque.
     */
    @Embedded
    private ProdutoEstoqueBase estoqueConfig;

    /**
     * Preço atual do produto.
     */
    @Embedded
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
