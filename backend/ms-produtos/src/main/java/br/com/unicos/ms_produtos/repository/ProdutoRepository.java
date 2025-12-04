package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas operações de persistência
 * da entidade {@link Produto}.
 *
 * <p>
 * Fornece métodos de consulta específicos para o catálogo de produtos,
 * respeitando a composição dos dados universais presentes em
 * {@link br.com.unicos.core.produto.model.ProdutoBase},
 * {@link br.com.unicos.core.produto.model.PrecoBase},
 * {@link br.com.unicos.core.produto.model.ProdutoEstoqueBase}
 * e {@link br.com.unicos.core.produto.model.ProdutoTributacaoBase}.
 * </p>
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // ============================================================
    // 🔍 Consultas por dados básicos (Embedded: dadosBasicos)
    // ============================================================

    /**
     * Busca produtos cujo nome contenha o termo informado (ignorando caixa).
     *
     * @param nome Parte do nome do produto.
     * @return Lista de produtos que possuem o nome semelhante.
     */
    List<Produto> findByDadosBasicosNomeContainingIgnoreCase(String nome);

    /**
     * Busca um produto pelo SKU global definido no core-produto.
     *
     * @param sku Código SKU único.
     * @return Produto correspondente, se existir.
     */
    Optional<Produto> findByDadosBasicosSku(String sku);

    /**
     * Busca produtos pelo código de barras (EAN/UPC).
     *
     * @param codigoBarras código de barras do produto
     * @return lista de produtos que possuem o código informado
     */
    List<Produto> findByDadosBasicosCodigoBarras(String codigoBarras);


    // ============================================================
    // 🔍 Consultas por atributos operacionais
    // ============================================================

    /**
     * Retorna todos os produtos ativos.
     *
     * @return Lista de produtos ativos.
     */
    List<Produto> findByAtivoTrue();

    /**
     * Retorna todos os produtos inativos.
     *
     * @return Lista de produtos inativos.
     */
    List<Produto> findByAtivoFalse();

    /**
     * Busca produtos pertencentes a uma categoria específica.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de produtos desta categoria.
     */
    List<Produto> findByCategoriaId(Long categoriaId);

    /**
     * Busca produtos pelo ID da marca cadastrada.
     *
     * @param marcaId ID da marca.
     * @return Lista de produtos da marca informada.
     */
    List<Produto> findByMarcaId(Long marcaId);


    // ============================================================
    // 🔍 Consultas por preço (Embedded: precoAtual)
    // ============================================================

    /**
     * Busca produtos cujo preço de venda esteja dentro da faixa informada.
     *
     * @param precoMin Valor mínimo.
     * @param precoMax Valor máximo.
     * @return Lista de produtos no intervalo de preço.
     */
    List<Produto> findByPrecoAtualPrecoVendaBetween(BigDecimal precoMin, BigDecimal precoMax);
}
