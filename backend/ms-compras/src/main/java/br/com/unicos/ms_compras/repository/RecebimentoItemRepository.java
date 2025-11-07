package br.com.unicos.ms_compras.repository;

import br.com.unicos.ms_compras.model.recebimento.RecebimentoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository responsável pela persistência e consultas de {@link RecebimentoItem}.
 */
@Repository
public interface RecebimentoItemRepository extends JpaRepository<RecebimentoItem, Long> {

    // -----------------------------------------------------------------------
    // Consultas diretas
    // -----------------------------------------------------------------------

    /**
     * Retorna todos os itens vinculados a um recebimento específico.
     *
     * @param recebimentoCompraId ID do recebimento de compra
     * @return lista de itens
     */
    List<RecebimentoItem> findByRecebimentoCompraId(Long recebimentoCompraId);

    /**
     * Retorna todos os itens de um determinado produto recebidos (em qualquer processo).
     *
     * @param produtoId ID do produto
     * @return lista de ocorrências do produto em recebimentos
     */
    List<RecebimentoItem> findByProdutoId(Long produtoId);

    // -----------------------------------------------------------------------
    // Consultas analíticas de conferência
    // -----------------------------------------------------------------------

    /**
     * Obtém a soma total da quantidade recebida de um produto em todos os recebimentos.
     *
     * @param produtoId ID do produto
     * @return total recebido
     */
    @Query("""
           SELECT COALESCE(SUM(i.quantidadeRecebida), 0)
             FROM RecebimentoItem i
            WHERE i.produtoId = :produtoId
           """)
    BigDecimal sumQuantidadeRecebidaPorProduto(Long produtoId);

    /**
     * Obtém a soma total da quantidade prevista em um recebimento.
     *
     * @param recebimentoCompraId ID do recebimento
     * @return total previsto
     */
    @Query("""
           SELECT COALESCE(SUM(i.quantidadePrevista), 0)
             FROM RecebimentoItem i
            WHERE i.recebimentoCompra.id = :recebimentoCompraId
           """)
    BigDecimal sumQuantidadePrevistaPorRecebimento(Long recebimentoCompraId);

    /**
     * Obtém a soma total da quantidade devolvida em um recebimento.
     *
     * @param recebimentoCompraId ID do recebimento
     * @return total devolvido
     */
    @Query("""
           SELECT COALESCE(SUM(i.quantidadeDevolvida), 0)
             FROM RecebimentoItem i
            WHERE i.recebimentoCompra.id = :recebimentoCompraId
           """)
    BigDecimal sumQuantidadeDevolvidaPorRecebimento(Long recebimentoCompraId);

    // -----------------------------------------------------------------------
    // Análises de divergência
    // -----------------------------------------------------------------------

    /**
     * Obtém a diferença total (recebido - previsto) de um recebimento.
     *
     * @param recebimentoCompraId ID do recebimento
     * @return diferença total entre o recebido e o previsto
     */
    @Query("""
           SELECT COALESCE(SUM(i.quantidadeRecebida - COALESCE(i.quantidadePrevista, 0)), 0)
             FROM RecebimentoItem i
            WHERE i.recebimentoCompra.id = :recebimentoCompraId
           """)
    BigDecimal calcularDivergenciaTotal(Long recebimentoCompraId);

    /**
     * Conta quantos itens possuem divergência (recebido ≠ previsto) em um recebimento.
     *
     * @param recebimentoCompraId ID do recebimento
     * @return número de itens divergentes
     */
    @Query("""
           SELECT COUNT(i)
             FROM RecebimentoItem i
            WHERE i.recebimentoCompra.id = :recebimentoCompraId
              AND (i.quantidadeRecebida <> COALESCE(i.quantidadePrevista, 0))
           """)
    long countItensDivergentes(Long recebimentoCompraId);
}
