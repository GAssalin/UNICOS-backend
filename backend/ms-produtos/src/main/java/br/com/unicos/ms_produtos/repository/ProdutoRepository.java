package br.com.unicos.ms_produtos.repository;

import br.com.unicos.ms_produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade Produto.
 *
 * Fornece métodos personalizados para consultas específicas,
 * além dos métodos CRUD padrão fornecidos pelo JpaRepository.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     * Busca um produto pelo seu SKU (Stock Keeping Unit).
     *
     * @param sku Código SKU do produto.
     * @return Optional contendo o produto, se encontrado.
     */
    Optional<Produto> findBySku(String sku);

    /**
     * Lista todos os produtos que estão ativos.
     *
     * @return Lista de produtos com o campo "ativo" igual a true.
     */
    List<Produto> findByAtivoTrue();

    /**
     * Lista todos os produtos que pertencem a uma determinada categoria.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de produtos pertencentes à categoria informada.
     */
    List<Produto> findByCategoriaId(Long categoriaId);

    /**
     * Lista todos os produtos associados a uma determinada marca.
     *
     * @param marcaId ID da marca.
     * @return Lista de produtos pertencentes à marca informada.
     */
    List<Produto> findByMarcaId(Long marcaId);

    /**
     * Busca produtos cujo nome contenha um determinado termo,
     * ignorando maiúsculas e minúsculas.
     *
     * @param nome Termo de busca.
     * @return Lista de produtos correspondentes ao termo informado.
     */
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    /**
     * Lista todos os produtos que estão inativos.
     *
     * @return Lista de produtos com o campo "ativo" igual a false.
     */
    List<Produto> findByAtivoFalse();

    /**
     * Busca produtos cujo preço esteja dentro de um intervalo específico.
     *
     * @param min Valor mínimo do preço.
     * @param max Valor máximo do preço.
     * @return Lista de produtos com preço entre os valores informados.
     */
    List<Produto> findByPrecoBetween(BigDecimal min, BigDecimal max);
}