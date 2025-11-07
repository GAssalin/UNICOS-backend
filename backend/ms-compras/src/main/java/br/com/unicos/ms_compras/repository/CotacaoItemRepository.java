package br.com.unicos.ms_compras.repository;

import br.com.unicos.ms_compras.model.cotacao.CotacaoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsável pela persistência e consultas de {@link CotacaoItem}.
 */
@Repository
public interface CotacaoItemRepository extends JpaRepository<CotacaoItem, Long> {

    // -----------------------------------------------------------------------
    // Consultas diretas
    // -----------------------------------------------------------------------

    /**
     * Busca todos os itens de uma cotação específica.
     *
     * @param cotacaoCompraId ID da cotação principal
     * @return lista de itens vinculados à cotação
     */
    @Query("""
            SELECT i
              FROM CotacaoItem i
             WHERE i.cotacaoFornecedor.cotacaoCompra.id = :cotacaoCompraId
            """)
    List<CotacaoItem> findByCotacaoCompraId(Long cotacaoCompraId);

    /**
     * Busca todos os itens cotados por um fornecedor específico.
     *
     * @param fornecedorId ID do fornecedor
     * @return lista de itens cotados pelo fornecedor
     */
    @Query("""
            SELECT i
              FROM CotacaoItem i
             WHERE i.cotacaoFornecedor.fornecedorId = :fornecedorId
            """)
    List<CotacaoItem> findByFornecedorId(Long fornecedorId);

    /**
     * Busca todos os itens de um determinado produto em uma cotação.
     *
     * @param cotacaoCompraId ID da cotação
     * @param produtoId       ID do produto
     * @return lista de itens correspondentes
     */
    @Query("""
            SELECT i
              FROM CotacaoItem i
             WHERE i.cotacaoFornecedor.cotacaoCompra.id = :cotacaoCompraId
               AND i.produtoId = :produtoId
            """)
    List<CotacaoItem> findByCotacaoCompraIdAndProdutoId(Long cotacaoCompraId, Long produtoId);

    /**
     * Busca o item de cotação específico de um fornecedor para um produto.
     *
     * @param fornecedorId ID do fornecedor
     * @param produtoId    ID do produto
     * @return item correspondente, se existir
     */
    @Query("""
            SELECT i
              FROM CotacaoItem i
             WHERE i.cotacaoFornecedor.fornecedorId = :fornecedorId
               AND i.produtoId = :produtoId
            """)
    Optional<CotacaoItem> findByFornecedorIdAndProdutoId(Long fornecedorId, Long produtoId);

    // -----------------------------------------------------------------------
    // Métricas e análises de valores
    // -----------------------------------------------------------------------

    /**
     * Obtém o menor valor unitário ofertado para um produto dentro de uma cotação.
     *
     * @param cotacaoCompraId ID da cotação
     * @param produtoId       ID do produto
     * @return menor valor unitário ofertado
     */
    @Query("""
            SELECT MIN(i.valorUnitario)
              FROM CotacaoItem i
             WHERE i.cotacaoFornecedor.cotacaoCompra.id = :cotacaoCompraId
               AND i.produtoId = :produtoId
            """)
    BigDecimal findMenorValorUnitario(Long cotacaoCompraId, Long produtoId);

    /**
     * Obtém o maior valor unitário ofertado para um produto dentro de uma cotação.
     *
     * @param cotacaoCompraId ID da cotação
     * @param produtoId       ID do produto
     * @return maior valor unitário ofertado
     */
    @Query("""
            SELECT MAX(i.valorUnitario)
              FROM CotacaoItem i
             WHERE i.cotacaoFornecedor.cotacaoCompra.id = :cotacaoCompraId
               AND i.produtoId = :produtoId
            """)
    BigDecimal findMaiorValorUnitario(Long cotacaoCompraId, Long produtoId);

    /**
     * Obtém a média de valores unitários para um produto em uma cotação.
     *
     * @param cotacaoCompraId ID da cotação
     * @param produtoId       ID do produto
     * @return média de valores unitários
     */
    @Query("""
            SELECT AVG(i.valorUnitario)
              FROM CotacaoItem i
             WHERE i.cotacaoFornecedor.cotacaoCompra.id = :cotacaoCompraId
               AND i.produtoId = :produtoId
            """)
    BigDecimal findMediaValorUnitario(Long cotacaoCompraId, Long produtoId);

    // -----------------------------------------------------------------------
    // Contagens
    // -----------------------------------------------------------------------

    /**
     * Conta quantos itens estão vinculados a uma cotação específica.
     *
     * @param cotacaoCompraId ID da cotação
     * @return total de itens
     */
    @Query("""
            SELECT COUNT(i)
              FROM CotacaoItem i
             WHERE i.cotacaoFornecedor.cotacaoCompra.id = :cotacaoCompraId
            """)
    long countByCotacaoCompraId(Long cotacaoCompraId);
}
