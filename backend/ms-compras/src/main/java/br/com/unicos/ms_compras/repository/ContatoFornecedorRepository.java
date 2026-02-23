package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.ContatoFornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ContatoFornecedor}.
 * <p>
 * Centraliza consultas relacionadas aos contatos vinculados aos fornecedores,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface ContatoFornecedorRepository extends BaseTenantRepository<ContatoFornecedor, Long> {

    /**
     * Lista contatos de um fornecedor dentro do tenant.
     */
    Page<ContatoFornecedor> findByFornecedorIdAndEmpresaId(
            Long fornecedorId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista todos os contatos de um fornecedor ordenados por nome.
     */
    List<ContatoFornecedor> findByFornecedorIdAndEmpresaIdOrderByNomeAsc(
            Long fornecedorId,
            Long tenantId
    );

    /**
     * Recupera um contato específico dentro do tenant.
     */
    Optional<ContatoFornecedor> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Recupera o contato principal do fornecedor dentro do tenant.
     */
    Optional<ContatoFornecedor> findByFornecedorIdAndPrincipalTrueAndEmpresaId(
            Long fornecedorId,
            Long tenantId
    );

    /**
     * Verifica se já existe outro contato principal para o fornecedor dentro do tenant.
     */
    boolean existsByFornecedorIdAndPrincipalTrueAndEmpresaId(
            Long fornecedorId,
            Long tenantId
    );

    /**
     * Remove todos os contatos de um fornecedor dentro do tenant.
     */
    void deleteByFornecedorIdAndEmpresaId(
            Long fornecedorId,
            Long tenantId
    );
}