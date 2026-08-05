package br.com.unicos.ms_cliente.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_cliente.enums.TipoObservacaoCliente;
import br.com.unicos.ms_cliente.model.ClienteObservacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pelo acesso às observações de clientes.
 */
@Repository
public interface ClienteObservacaoRepository extends BaseTenantRepository<ClienteObservacao, Long> {

    /**
     * Lista observações de um cliente.
     *
     * @param clienteId identificador do cliente
     * @param tenantId identificador da empresa
     * @param pageable paginação
     * @return página de observações
     */
    Page<ClienteObservacao> findByClienteIdAndEmpresaId(Long clienteId, Long tenantId, Pageable pageable);

    /**
     * Lista observações por tipo de um cliente.
     *
     * @param clienteId identificador do cliente
     * @param tipo tipo da observação
     * @param tenantId identificador da empresa
     * @param pageable paginação
     * @return página de observações
     */
    Page<ClienteObservacao> findByClienteIdAndTipoAndEmpresaId(Long clienteId, TipoObservacaoCliente tipo, Long tenantId, Pageable pageable);
}