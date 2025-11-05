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
 * <p>
 * Fornece métodos para consultas personalizadas no catálogo de produtos.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     * Busca produtos cujo nome contenha o termo informado (case insensitive).
     *
     * @param nome Parte do nome do produto.
     * @return Lista de produtos correspondentes.
     */
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    /**
     * Retorna todos os produtos ativos.
     *
     * @return Lista de produtos ativos.
     */
    List<Produto> findByAtivoTrue();

    /**
     * Busca produtos pertencentes a uma categoria específica.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de produtos da categoria.
     */
    List<Produto> findByCategoriaId(Long categoriaId);

    /**
     * Busca produtos por marca.
     *
     * @param marcaId ID da marca.
     * @return Lista de produtos da marca.
     */
    List<Produto> findByMarcaId(Long marcaId);

    /**
     * Busca um produto pelo SKU.
     *
     * @param sku Código SKU único.
     * @return Produto correspondente, se existir.
     */
    Optional<Produto> findBySku(String sku);

    /**
     * Busca produtos dentro de uma faixa de preço.
     *
     * @param precoMin Valor mínimo.
     * @param precoMax Valor máximo.
     * @return Lista de produtos no intervalo.
     */
    List<Produto> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax);
}
