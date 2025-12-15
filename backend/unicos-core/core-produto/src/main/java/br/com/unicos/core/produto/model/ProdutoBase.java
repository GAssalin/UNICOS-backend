package br.com.unicos.core.produto.model;

import br.com.unicos.core.produto.enums.*;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Modelo base que representa as informações essenciais e globais de um produto no sistema.
 *
 * <p>
 * Este modelo é utilizado pelos microserviços para padronizar o tráfego de dados referentes
 * a produtos, evitando acoplamento direto ao <b>ms-produtos</b> e garantindo consistência
 * entre módulos como estoque, compras, catálogo e vendas.
 * </p>
 *
 * <p>
 * Importante: esta classe não é uma entidade JPA, não possui regras de negócio
 * e não deve ser persistida diretamente. Seu papel é exclusivamente transportar
 * dados padronizados no ecossistema UNICOS.
 * </p>
 */
@Embeddable
public class ProdutoBase {

    /**
     * Nome oficial do produto no catálogo global.
     */
    private String nome;

    /**
     * Descrição detalhada do produto, usada para fins operacionais e exibição.
     */
    private String descricao;

    /**
     * Código SKU (Stock Keeping Unit) padronizado.
     */
    private String sku;

    /**
     * Classificação global do produto (simples, composto, serviço etc.).
     */
    @Enumerated(EnumType.STRING)
    private TipoProduto tipoProduto;

    /**
     * Tipo de variação suportado pelo produto (cor, tamanho, ambos etc.).
     */
    @Enumerated(EnumType.STRING)
    private TipoVariacaoProduto tipoVariacaoProduto;

    /**
     * Origem do produto (nacional, importado etc.).
     */
    @Enumerated(EnumType.STRING)
    private TipoOrigemProduto tipoOrigemProduto;

    /**
     * Forma de controle de estoque utilizada para o produto.
     */
    @Enumerated(EnumType.STRING)
    private TipoControleEstoque tipoControleEstoque;

    /**
     * Tipo de armazenamento necessário para o produto (normal, refrigerado etc.).
     */
    @Enumerated(EnumType.STRING)
    private TipoArmazenamentoProduto tipoArmazenamentoProduto;

    /**
     * Classificação geral do produto (acabado, matéria-prima, consumo etc.).
     */
    @Enumerated(EnumType.STRING)
    private TipoClassificacaoProduto tipoClassificacaoProduto;

    /**
     * Status operacional global do produto.
     */
    @Enumerated(EnumType.STRING)
    private StatusProduto statusProduto;

    public ProdutoBase() {
    }

    public ProdutoBase(
            String nome,
            String descricao,
            String sku,
            TipoProduto tipoProduto,
            TipoVariacaoProduto tipoVariacaoProduto,
            TipoOrigemProduto tipoOrigemProduto,
            TipoControleEstoque tipoControleEstoque,
            TipoArmazenamentoProduto tipoArmazenamentoProduto,
            TipoClassificacaoProduto tipoClassificacaoProduto,
            StatusProduto statusProduto
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.sku = sku;
        this.tipoProduto = tipoProduto;
        this.tipoVariacaoProduto = tipoVariacaoProduto;
        this.tipoOrigemProduto = tipoOrigemProduto;
        this.tipoControleEstoque = tipoControleEstoque;
        this.tipoArmazenamentoProduto = tipoArmazenamentoProduto;
        this.tipoClassificacaoProduto = tipoClassificacaoProduto;
        this.statusProduto = statusProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public TipoVariacaoProduto getTipoVariacaoProduto() {
        return tipoVariacaoProduto;
    }

    public void setTipoVariacaoProduto(TipoVariacaoProduto tipoVariacaoProduto) {
        this.tipoVariacaoProduto = tipoVariacaoProduto;
    }

    public TipoOrigemProduto getTipoOrigemProduto() {
        return tipoOrigemProduto;
    }

    public void setTipoOrigemProduto(TipoOrigemProduto tipoOrigemProduto) {
        this.tipoOrigemProduto = tipoOrigemProduto;
    }

    public TipoControleEstoque getTipoControleEstoque() {
        return tipoControleEstoque;
    }

    public void setTipoControleEstoque(TipoControleEstoque tipoControleEstoque) {
        this.tipoControleEstoque = tipoControleEstoque;
    }

    public TipoArmazenamentoProduto getTipoArmazenamentoProduto() {
        return tipoArmazenamentoProduto;
    }

    public void setTipoArmazenamentoProduto(TipoArmazenamentoProduto tipoArmazenamentoProduto) {
        this.tipoArmazenamentoProduto = tipoArmazenamentoProduto;
    }

    public TipoClassificacaoProduto getTipoClassificacaoProduto() {
        return tipoClassificacaoProduto;
    }

    public void setTipoClassificacaoProduto(TipoClassificacaoProduto tipoClassificacaoProduto) {
        this.tipoClassificacaoProduto = tipoClassificacaoProduto;
    }

    public StatusProduto getStatusProduto() {
        return statusProduto;
    }

    public void setStatusProduto(StatusProduto statusProduto) {
        this.statusProduto = statusProduto;
    }
}
