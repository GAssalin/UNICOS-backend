package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.enums.StatusPedidoCompra;
import br.com.unicos.ms_compras.model.PedidoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link PedidoCompra}.
 * <p>
 * Centraliza consultas relacionadas aos pedidos de compra,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface PedidoCompraRepository extends BaseTenantRepository<PedidoCompra, Long> {

    /**
     * Recupera pedido por ID dentro do tenant.
     */
    Optional<PedidoCompra> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Recupera pedido por código dentro do tenant.
     */
    Optional<PedidoCompra> findByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Verifica se já existe pedido com o mesmo código dentro do tenant.
     */
    boolean existsByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Lista pedidos por fornecedor dentro do tenant.
     */
    Page<PedidoCompra> findByFornecedorIdAndEmpresaId(
            Long fornecedorId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista pedidos por status dentro do tenant.
     */
    Page<PedidoCompra> findByStatusPedidoCompraAndEmpresaId(
            StatusPedidoCompra statusPedidoCompra,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista pedidos por período de emissão dentro do tenant.
     */
    Page<PedidoCompra> findByDataEmissaoBetweenAndEmpresaId(
            LocalDate dataInicial,
            LocalDate dataFinal,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista pedidos com entrega prevista até uma data limite dentro do tenant.
     */
    Page<PedidoCompra> findByDataPrevistaEntregaLessThanEqualAndEmpresaId(
            LocalDate dataLimite,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista pedidos por condição de pagamento dentro do tenant.
     */
    Page<PedidoCompra> findByCondicaoPagamentoIdAndEmpresaId(
            Long condicaoPagamentoId,
            Long tenantId,
            Pageable pageable
    );
}