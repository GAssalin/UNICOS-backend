package br.com.unicos.ms_cliente.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_cliente.enums.StatusCliente;
import br.com.unicos.ms_cliente.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Cliente}.
 *
 * <p>
 * Centraliza consultas relacionadas ao cadastro comercial de clientes,
 * respeitando o contexto multi-tenant.
 * </p>
 */
@Repository
public interface ClienteRepository extends BaseTenantRepository<Cliente, Long> {

    /**
     * Busca cliente por vendedor responsável dentro do tenant.
     *
     * @param tenantId identificador da empresa (tenant)
     * @param vendedorId identificador do vendedor
     * @return cliente encontrado
     */
    Page<Cliente> findByEmpresaIdAndVendedorId(Long tenantId, Long vendedorId, Pageable pageable);

    /**
     * Lista clientes por status dentro do tenant.
     *
     * @param status status do cliente
     * @param tenantId identificador da empresa
     * @param pageable paginação
     * @return página de clientes
     */
    Page<Cliente> findByStatusAndEmpresaId(StatusCliente status, Long tenantId, Pageable pageable);

    /**
     * Verifica se já existe cliente para uma pessoa dentro do tenant.
     *
     * @param pessoaId identificador da pessoa
     * @param tenantId identificador da empresa
     * @return true se existir
     */
    boolean existsByPessoaIdAndEmpresaId(Long pessoaId, Long tenantId);
}