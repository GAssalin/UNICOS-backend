package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.EnderecoFornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EnderecoFornecedor}.
 * <p>
 * Centraliza consultas relacionadas aos endereços dos fornecedores,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface EnderecoFornecedorRepository extends BaseTenantRepository<EnderecoFornecedor, Long> {

    /**
     * Recupera endereço por ID dentro do tenant.
     */
    Optional<EnderecoFornecedor> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Lista endereços de um fornecedor dentro do tenant.
     */
    Page<EnderecoFornecedor> findByFornecedorIdAndEmpresaId(
            Long fornecedorId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista todos os endereços de um fornecedor ordenados por tipo.
     */
    List<EnderecoFornecedor> findByFornecedorIdAndEmpresaIdOrderByTipoAsc(
            Long fornecedorId,
            Long tenantId
    );

    /**
     * Recupera endereço por tipo dentro do fornecedor e tenant.
     */
    Optional<EnderecoFornecedor> findByFornecedorIdAndTipoAndEmpresaId(
            Long fornecedorId,
            String tipo,
            Long tenantId
    );

    /**
     * Verifica se já existe endereço do mesmo tipo para o fornecedor dentro do tenant.
     */
    boolean existsByFornecedorIdAndTipoAndEmpresaId(
            Long fornecedorId,
            String tipo,
            Long tenantId
    );

    /**
     * Lista endereços por CEP dentro do tenant.
     */
    Page<EnderecoFornecedor> findByCepAndEmpresaId(
            String cep,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Remove todos os endereços de um fornecedor dentro do tenant.
     */
    void deleteByFornecedorIdAndEmpresaId(
            Long fornecedorId,
            Long tenantId
    );
}