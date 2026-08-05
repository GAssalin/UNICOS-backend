package br.com.unicos.ms_cliente.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_cliente.model.ClienteHistoricoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pelo histórico de status do cliente.
 */
@Repository
public interface ClienteHistoricoStatusRepository extends BaseTenantRepository<ClienteHistoricoStatus, Long> {

    /**
     * Lista histórico de status de um cliente.
     *
     * @param clienteId identificador do cliente
     * @param tenantId identificador da empresa
     * @param pageable paginação
     * @return página de histórico
     */
    Page<ClienteHistoricoStatus> findByClienteIdAndEmpresaId(Long clienteId, Long tenantId, Pageable pageable);
}