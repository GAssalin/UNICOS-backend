package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.ItemPedidoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ItemPedidoCompra}.
 * <p>
 * Centraliza consultas relacionadas aos itens dos pedidos de compra,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface ItemPedidoCompraRepository extends BaseTenantRepository<ItemPedidoCompra, Long> {

    /**
     * Recupera item por ID dentro do tenant.
     */
    Optional<ItemPedidoCompra> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Lista itens de um pedido de compra dentro do tenant.
     */
    Page<ItemPedidoCompra> findByPedidoCompraIdAndEmpresaId(
            Long pedidoCompraId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista todos os itens de um pedido ordenados por ID.
     */
    List<ItemPedidoCompra> findByPedidoCompraIdAndEmpresaIdOrderByIdAsc(
            Long pedidoCompraId,
            Long tenantId
    );

    /**
     * Lista itens de um pedido filtrando por produto dentro do tenant.
     */
    Page<ItemPedidoCompra> findByPedidoCompraIdAndProdutoIdAndEmpresaId(
            Long pedidoCompraId,
            Long produtoId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Verifica se existe item para um produto dentro do pedido no tenant.
     */
    boolean existsByPedidoCompraIdAndProdutoIdAndEmpresaId(
            Long pedidoCompraId,
            Long produtoId,
            Long tenantId
    );

    /**
     * Remove todos os itens de um pedido dentro do tenant.
     */
    void deleteByPedidoCompraIdAndEmpresaId(
            Long pedidoCompraId,
            Long tenantId
    );
}