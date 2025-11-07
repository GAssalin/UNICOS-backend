package br.com.unicos.ms_compras.repository;

import br.com.unicos.ms_compras.enums.StatusPedidoCompra;
import br.com.unicos.ms_compras.model.pedido.PedidoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsável pelo gerenciamento de {@link PedidoCompra}.
 */
@Repository
public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {

    // -----------------------------------------------------------------------
    // Consultas diretas
    // -----------------------------------------------------------------------

    /**
     * Busca um pedido de compra pelo identificador do fornecedor e código interno.
     *
     * @param fornecedorId ID do fornecedor
     * @param codigo       código único do pedido
     * @return pedido encontrado, se existir
     */
    Optional<PedidoCompra> findByFornecedorIdAndCodigo(Long fornecedorId, String codigo);

    /**
     * Lista todos os pedidos de compra de um fornecedor específico.
     *
     * @param fornecedorId ID do fornecedor
     * @return lista de pedidos do fornecedor
     */
    List<PedidoCompra> findByFornecedorId(Long fornecedorId);

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
     * @param inicio data inicial (inclusive)
     * @param fim    data final (inclusive)
     * @return lista de pedidos do período
     */
    @Query("""
            SELECT p
              FROM PedidoCompra p
             WHERE p.dataCriacao BETWEEN :inicio AND :fim
            """)
    List<PedidoCompra> findByPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Busca paginada de pedidos com filtro por termo textual (ex.: observação ou código).
     *
     * @param termo    termo de busca
     * @param pageable parâmetros de paginação
     * @return página de resultados
     */
    @Query("""
            SELECT p
              FROM PedidoCompra p
             WHERE LOWER(p.observacao) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :termo, '%'))
            """)
    Page<PedidoCompra> search(String termo, Pageable pageable);

    // -----------------------------------------------------------------------
    // Agregações e métricas
    // -----------------------------------------------------------------------

    /**
     * Obtém o valor total dos pedidos de um fornecedor dentro de um período.
     *
     * @param fornecedorId ID do fornecedor
     * @param inicio       data inicial
     * @param fim          data final
     * @return soma dos valores totais
     */
    @Query("""
            SELECT COALESCE(SUM(p.valorTotal), 0)
              FROM PedidoCompra p
             WHERE p.fornecedorId = :fornecedorId
               AND p.dataCriacao BETWEEN :inicio AND :fim
            """)
    BigDecimal sumValorTotalByFornecedorAndPeriodo(Long fornecedorId, LocalDate inicio, LocalDate fim);

    /**
     * Conta quantos pedidos estão em um determinado status.
     *
     * @param status status do pedido
     * @return total de pedidos
     */
    long countByStatus(StatusPedidoCompra status);
}
