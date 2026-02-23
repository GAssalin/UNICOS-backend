package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.ItemRecebimentoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ItemRecebimentoCompra}.
 * <p>
 * Centraliza consultas relacionadas aos itens recebidos nas compras,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface ItemRecebimentoCompraRepository extends BaseTenantRepository<ItemRecebimentoCompra, Long> {

    /**
     * Recupera item recebido por ID dentro do tenant.
     */
    Optional<ItemRecebimentoCompra> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Lista itens de um recebimento dentro do tenant.
     */
    Page<ItemRecebimentoCompra> findByRecebimentoCompraIdAndEmpresaId(
            Long recebimentoCompraId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista todos os itens de um recebimento ordenados por ID.
     */
    List<ItemRecebimentoCompra> findByRecebimentoCompraIdAndEmpresaIdOrderByIdAsc(
            Long recebimentoCompraId,
            Long tenantId
    );

    /**
     * Recupera item recebido a partir do item do pedido dentro do tenant.
     */
    Optional<ItemRecebimentoCompra> findByItemPedidoCompraIdAndEmpresaId(
            Long itemPedidoCompraId,
            Long tenantId
    );

    /**
     * Verifica se já existe recebimento para o item do pedido dentro do tenant.
     */
    boolean existsByItemPedidoCompraIdAndEmpresaId(
            Long itemPedidoCompraId,
            Long tenantId
    );

    /**
     * Remove todos os itens de um recebimento dentro do tenant.
     */
    void deleteByRecebimentoCompraIdAndEmpresaId(
            Long recebimentoCompraId,
            Long tenantId
    );
}