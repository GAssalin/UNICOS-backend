package br.com.unicos.ms_compras.repository.pedido;

import br.com.unicos.ms_compras.enums.StatusPedidoCompra;
import br.com.unicos.ms_compras.model.pedido.PedidoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo gerenciamento de {@link PedidoCompra}.
 *
 * <p>
 * Fornece consultas específicas relacionadas ao ciclo de vida dos pedidos de compra,
 * permitindo filtros por fornecedor, status, período e texto livre.
 * </p>
 */
@Repository
public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {

    // -----------------------------------------------------------------------
    // Consultas diretas
    // -----------------------------------------------------------------------

    /**
     * Busca um pedido de compra pelo identificador do fornecedor e ID do pedido.
     *
     * @param fornecedorId ID do fornecedor
     * @param id           identificador único do pedido
     * @return pedido encontrado, se existir
     */
    Optional<PedidoCompra> findByFornecedorIdAndId(Long fornecedorId, Long id);

    /**
     * Lista todos os pedidos de compra de um fornecedor específico.
     *
     * @param fornecedorId ID do fornecedor
     * @return lista de pedidos do fornecedor
     */
    List<PedidoCompra> findByFornecedorId(Long fornecedorId);

    /**
     * Retorna todos os pedidos de compra criados dentro de um intervalo de datas.
     *
     * @param inicio data/hora inicial
     * @param fim    data/hora final
     * @return lista de pedidos dentro do intervalo informado
     */
    List<PedidoCompra> findByDataCriacaoBetween(LocalDateTime inicio, LocalDateTime fim);

    // -----------------------------------------------------------------------
    // Filtros de domínio
    // -----------------------------------------------------------------------

    /**
     * Lista pedidos pelo status atual.
     *
     * @param status status do pedido
     * @return lista de pedidos
     */
    List<PedidoCompra> findByStatus(StatusPedidoCompra status);

    /**
     * Busca pedidos de compra com um determinado status e fornecedor.
     *
     * @param fornecedorId ID do fornecedor
     * @param status       status do pedido
     * @return lista de pedidos
     */
    List<PedidoCompra> findByFornecedorIdAndStatus(Long fornecedorId, StatusPedidoCompra status);

    // -----------------------------------------------------------------------
    // Consultas por período e paginação
    // -----------------------------------------------------------------------

    /**
     * Retorna todos os pedidos criados dentro de um intervalo de datas.
     *
     * @param inicio data/hora inicial (inclusive)
     * @param fim    data/hora final (inclusive)
     * @return lista de pedidos do período
     */
    @Query("""
            SELECT p
              FROM PedidoCompra p
             WHERE p.dataCriacao BETWEEN :inicio AND :fim
            """)
    List<PedidoCompra> findByPeriodo(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Busca paginada de pedidos com filtro por termo textual (ex.: observação).
     *
     * @param termo    termo de busca
     * @param pageable parâmetros de paginação
     * @return página de resultados
     */
    @Query("""
            SELECT p
              FROM PedidoCompra p
             WHERE LOWER(p.observacao) LIKE LOWER(CONCAT('%', :termo, '%'))
            """)
    Page<PedidoCompra> search(String termo, Pageable pageable);

    // -----------------------------------------------------------------------
    // Agregações e métricas
    // -----------------------------------------------------------------------

    /**
     * Obtém o valor total dos pedidos de um fornecedor dentro de um período.
     *
     * @param fornecedorId ID do fornecedor
     * @param inicio       data/hora inicial
     * @param fim          data/hora final
     * @return soma dos valores totais
     */
    @Query("""
            SELECT COALESCE(SUM(p.valorTotal), 0)
              FROM PedidoCompra p
             WHERE p.fornecedorId = :fornecedorId
               AND p.dataCriacao BETWEEN :inicio AND :fim
            """)
    BigDecimal sumValorTotalByFornecedorAndPeriodo(Long fornecedorId, LocalDateTime inicio, LocalDateTime fim);

    /**
     * Conta quantos pedidos estão em um determinado status.
     *
     * @param status status do pedido
     * @return total de pedidos
     */
    long countByStatus(StatusPedidoCompra status);
}
