package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.RecebimentoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link RecebimentoCompra}.
 * <p>
 * Centraliza consultas relacionadas aos recebimentos de compras,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface RecebimentoCompraRepository extends BaseTenantRepository<RecebimentoCompra, Long> {

    /**
     * Recupera recebimento por ID dentro do tenant.
     */
    Optional<RecebimentoCompra> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Lista recebimentos por pedido de compra dentro do tenant.
     */
    Page<RecebimentoCompra> findByPedidoCompraIdAndEmpresaId(
            Long pedidoCompraId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista recebimentos por fornecedor dentro do tenant.
     */
    Page<RecebimentoCompra> findByFornecedorIdAndEmpresaId(
            Long fornecedorId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista recebimentos por status dentro do tenant.
     */
    Page<RecebimentoCompra> findByStatusAndEmpresaId(
            String status,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista recebimentos por período de data dentro do tenant.
     */
    Page<RecebimentoCompra> findByDataRecebimentoBetweenAndEmpresaId(
            LocalDate dataInicial,
            LocalDate dataFinal,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Verifica se existe recebimento vinculado a um pedido dentro do tenant.
     */
    boolean existsByPedidoCompraIdAndEmpresaId(
            Long pedidoCompraId,
            Long tenantId
    );
}