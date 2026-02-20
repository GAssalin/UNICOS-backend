package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.DivergenciaRecebimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link DivergenciaRecebimento}.
 * <p>
 * Centraliza consultas relacionadas às divergências identificadas no recebimento de compras,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface DivergenciaRecebimentoRepository extends BaseTenantRepository<DivergenciaRecebimento, Long> {

    /**
     * Recupera divergência por ID dentro do tenant.
     */
    Optional<DivergenciaRecebimento> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Lista divergências de um item de recebimento dentro do tenant.
     */
    Page<DivergenciaRecebimento> findByItemRecebimentoCompraIdAndEmpresaId(
            Long itemRecebimentoCompraId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista todas as divergências de um item de recebimento ordenadas por ID.
     */
    List<DivergenciaRecebimento> findByItemRecebimentoCompraIdAndEmpresaIdOrderByIdAsc(
            Long itemRecebimentoCompraId,
            Long tenantId
    );

    /**
     * Lista divergências por tipo dentro do tenant.
     */
    Page<DivergenciaRecebimento> findByTipoAndEmpresaId(
            String tipo,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Verifica se existe divergência para um item de recebimento dentro do tenant.
     */
    boolean existsByItemRecebimentoCompraIdAndEmpresaId(
            Long itemRecebimentoCompraId,
            Long tenantId
    );

    /**
     * Remove todas as divergências de um item de recebimento dentro do tenant.
     */
    void deleteByItemRecebimentoCompraIdAndEmpresaId(
            Long itemRecebimentoCompraId,
            Long tenantId
    );
}