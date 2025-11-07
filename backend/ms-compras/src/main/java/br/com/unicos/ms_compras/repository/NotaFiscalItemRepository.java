package br.com.unicos.ms_compras.repository;

import br.com.unicos.ms_compras.model.fiscal.NotaFiscalItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository responsável pelas operações de persistência e consulta
 * da entidade {@link NotaFiscalItem}.
 */
@Repository
public interface NotaFiscalItemRepository extends JpaRepository<NotaFiscalItem, Long> {

    // -----------------------------------------------------------------------
    // Consultas diretas
    // -----------------------------------------------------------------------

    /**
     * Retorna todos os itens pertencentes a uma nota fiscal específica.
     *
     * @param notaFiscalCompraId ID da nota fiscal de compra
     * @return lista de itens vinculados
     */
    List<NotaFiscalItem> findByNotaFiscalCompraId(Long notaFiscalCompraId);

    /**
     * Retorna todos os itens de um determinado produto, independente da nota.
     *
     * @param produtoId ID do produto
     * @return lista de itens correspondentes
     */
    List<NotaFiscalItem> findByProdutoId(Long produtoId);

    // -----------------------------------------------------------------------
    // Agregações de valores
    // -----------------------------------------------------------------------

    /**
     * Soma o valor total de todos os itens de uma nota fiscal.
     *
     * @param notaFiscalCompraId ID da nota fiscal
     * @return soma dos valores totais dos itens
     */
    @Query("""
            SELECT COALESCE(SUM(i.valorTotal), 0)
              FROM NotaFiscalItem i
             WHERE i.notaFiscalCompra.id = :notaFiscalCompraId
            """)
    BigDecimal sumValorTotalByNotaFiscal(Long notaFiscalCompraId);

    /**
     * Soma o valor total de ICMS dos itens de uma nota fiscal.
     *
     * @param notaFiscalCompraId ID da nota fiscal
     * @return soma do ICMS
     */
    @Query("""
            SELECT COALESCE(SUM(i.valorICMS), 0)
              FROM NotaFiscalItem i
             WHERE i.notaFiscalCompra.id = :notaFiscalCompraId
            """)
    BigDecimal sumValorICMSByNotaFiscal(Long notaFiscalCompraId);

    /**
     * Soma o valor total de IPI dos itens de uma nota fiscal.
     *
     * @param notaFiscalCompraId ID da nota fiscal
     * @return soma do IPI
     */
    @Query("""
            SELECT COALESCE(SUM(i.valorIPI), 0)
              FROM NotaFiscalItem i
             WHERE i.notaFiscalCompra.id = :notaFiscalCompraId
            """)
    BigDecimal sumValorIPIByNotaFiscal(Long notaFiscalCompraId);

    /**
     * Soma o valor total de descontos aplicados nos itens de uma nota fiscal.
     *
     * @param notaFiscalCompraId ID da nota fiscal
     * @return soma dos descontos
     */
    @Query("""
            SELECT COALESCE(SUM(i.valorDesconto), 0)
              FROM NotaFiscalItem i
             WHERE i.notaFiscalCompra.id = :notaFiscalCompraId
            """)
    BigDecimal sumValorDescontoByNotaFiscal(Long notaFiscalCompraId);

    // -----------------------------------------------------------------------
    // Consultas analíticas
    // -----------------------------------------------------------------------

    /**
     * Obtém a média de valor unitário dos itens de um produto específico.
     *
     * @param produtoId ID do produto
     * @return média de valor unitário
     */
    @Query("""
            SELECT COALESCE(AVG(i.valorUnitario), 0)
              FROM NotaFiscalItem i
             WHERE i.produtoId = :produtoId
            """)
    BigDecimal findMediaValorUnitarioPorProduto(Long produtoId);

    /**
     * Conta quantos itens de determinado produto existem em todas as notas fiscais.
     *
     * @param produtoId ID do produto
     * @return total de ocorrências do produto
     */
    long countByProdutoId(Long produtoId);
}
