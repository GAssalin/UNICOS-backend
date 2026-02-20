package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.CotacaoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link CotacaoCompra}.
 * <p>
 * Centraliza consultas relacionadas às cotações de compra,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface CotacaoCompraRepository extends BaseTenantRepository<CotacaoCompra, Long> {

    /**
     * Recupera cotação por ID dentro do tenant.
     */
    Optional<CotacaoCompra> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Recupera cotação por código dentro do tenant.
     */
    Optional<CotacaoCompra> findByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Verifica se já existe cotação com o mesmo código dentro do tenant.
     */
    boolean existsByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Lista cotações por status dentro do tenant.
     */
    Page<CotacaoCompra> findByStatusAndEmpresaId(
            String status,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista cotações por período de abertura dentro do tenant.
     */
    Page<CotacaoCompra> findByDataAberturaBetweenAndEmpresaId(
            LocalDate dataInicial,
            LocalDate dataFinal,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista cotações próximas do vencimento (por data de validade) dentro do tenant.
     */
    Page<CotacaoCompra> findByDataValidadeLessThanEqualAndEmpresaId(
            LocalDate dataLimite,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista cotações vinculadas a um pedido de compra dentro do tenant.
     */
    Page<CotacaoCompra> findByPedidoCompraIdAndEmpresaId(
            Long pedidoCompraId,
            Long tenantId,
            Pageable pageable
    );
}