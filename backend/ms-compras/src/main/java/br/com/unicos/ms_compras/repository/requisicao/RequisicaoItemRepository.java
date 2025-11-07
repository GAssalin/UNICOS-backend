package br.com.unicos.ms_compras.repository.requisicao;

import br.com.unicos.ms_compras.model.requisicao.RequisicaoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository responsável pela persistência e consultas de {@link RequisicaoItem}.
 */
@Repository
public interface RequisicaoItemRepository extends JpaRepository<RequisicaoItem, Long> {

    // -----------------------------------------------------------------------
    // Consultas diretas
    // -----------------------------------------------------------------------

    /**
     * Retorna todos os itens de uma requisição de compra específica.
     *
     * @param requisicaoCompraId ID da requisição de compra
     * @return lista de itens vinculados
     */
    List<RequisicaoItem> findByRequisicaoCompraId(Long requisicaoCompraId);

    /**
     * Retorna todos os itens de um determinado produto em todas as requisições.
     *
     * @param produtoId ID do produto
     * @return lista de ocorrências do produto
     */
    List<RequisicaoItem> findByProdutoId(Long produtoId);

    // -----------------------------------------------------------------------
    // Consultas analíticas e métricas
    // -----------------------------------------------------------------------

    /**
     * Soma a quantidade total solicitada de um produto em todas as requisições.
     *
     * @param produtoId ID do produto
     * @return soma total das quantidades solicitadas
     */
    @Query("""
           SELECT COALESCE(SUM(i.quantidadeSolicitada), 0)
             FROM RequisicaoItem i
            WHERE i.produtoId = :produtoId
           """)
    BigDecimal sumQuantidadeSolicitadaPorProduto(Long produtoId);

    /**
     * Soma a quantidade total atendida (já comprada) de um produto.
     *
     * @param produtoId ID do produto
     * @return soma total das quantidades atendidas
     */
    @Query("""
           SELECT COALESCE(SUM(i.quantidadeAtendida), 0)
             FROM RequisicaoItem i
            WHERE i.produtoId = :produtoId
           """)
    BigDecimal sumQuantidadeAtendidaPorProduto(Long produtoId);

    /**
     * Calcula a diferença entre o solicitado e o atendido
     * para todos os itens de uma requisição.
     *
     * @param requisicaoCompraId ID da requisição
     * @return diferença total entre solicitado e atendido
     */
    @Query("""
           SELECT COALESCE(SUM(i.quantidadeSolicitada - COALESCE(i.quantidadeAtendida, 0)), 0)
             FROM RequisicaoItem i
            WHERE i.requisicaoCompra.id = :requisicaoCompraId
           """)
    BigDecimal calcularDiferencaTotalPorRequisicao(Long requisicaoCompraId);

    /**
     * Conta quantos itens de uma requisição ainda não foram atendidos totalmente.
     *
     * @param requisicaoCompraId ID da requisição
     * @return número de itens pendentes
     */
    @Query("""
           SELECT COUNT(i)
             FROM RequisicaoItem i
            WHERE i.requisicaoCompra.id = :requisicaoCompraId
              AND (i.quantidadeAtendida IS NULL OR i.quantidadeAtendida < i.quantidadeSolicitada)
           """)
    long countItensPendentesPorRequisicao(Long requisicaoCompraId);

    // -----------------------------------------------------------------------
    // Consultas de acompanhamento operacional
    // -----------------------------------------------------------------------

    /**
     * Retorna todos os itens que possuem divergência entre quantidade solicitada e atendida.
     *
     * @param requisicaoCompraId ID da requisição
     * @return lista de itens divergentes
     */
    @Query("""
           SELECT i
             FROM RequisicaoItem i
            WHERE i.requisicaoCompra.id = :requisicaoCompraId
              AND (i.quantidadeAtendida IS NULL OR i.quantidadeAtendida <> i.quantidadeSolicitada)
           """)
    List<RequisicaoItem> findItensDivergentesPorRequisicao(Long requisicaoCompraId);
}
