package br.com.unicos.ms_filial.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_filial.model.FilialStatusHistorico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link FilialStatusHistorico}.
 * <p>
 * Centraliza consultas de histórico de status de filiais,
 * respeitando o contexto multi-tenant.
 * </p>
 */
@Repository
public interface FilialStatusHistoricoRepository extends BaseTenantRepository<FilialStatusHistorico, Long> {

    /**
     * Lista históricos por filial dentro do tenant com paginação.
     */
    Page<FilialStatusHistorico> findByFilialIdAndEmpresaId(
            Long filialId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera histórico por id e filial dentro do tenant.
     */
    Optional<FilialStatusHistorico> findByIdAndFilialIdAndEmpresaId(
            Long id,
            Long filialId,
            Long tenantId
    );
}
