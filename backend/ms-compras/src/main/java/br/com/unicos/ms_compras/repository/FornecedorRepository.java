package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Fornecedor}.
 * <p>
 * Centraliza consultas relacionadas ao cadastro de fornecedores,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface FornecedorRepository extends BaseTenantRepository<Fornecedor, Long> {

    /**
     * Recupera fornecedor por ID dentro do tenant.
     */
    Optional<Fornecedor> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Recupera fornecedor por código dentro do tenant.
     */
    Optional<Fornecedor> findByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Recupera fornecedor por CNPJ dentro do tenant.
     */
    Optional<Fornecedor> findByCnpjAndEmpresaId(
            String cnpj,
            Long tenantId
    );

    /**
     * Verifica se já existe fornecedor com o mesmo código dentro do tenant.
     */
    boolean existsByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Verifica se já existe fornecedor com o mesmo CNPJ dentro do tenant.
     */
    boolean existsByCnpjAndEmpresaId(
            String cnpj,
            Long tenantId
    );

    /**
     * Pesquisa fornecedores por razão social (contém, ignorando maiúsculas/minúsculas).
     */
    Page<Fornecedor> findByRazaoSocialContainingIgnoreCaseAndEmpresaId(
            String razaoSocial,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Pesquisa fornecedores por nome fantasia (contém, ignorando maiúsculas/minúsculas).
     */
    Page<Fornecedor> findByNomeFantasiaContainingIgnoreCaseAndEmpresaId(
            String nomeFantasia,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Pesquisa fornecedores por CNPJ parcial.
     */
    Page<Fornecedor> findByCnpjContainingAndEmpresaId(
            String cnpj,
            Long tenantId,
            Pageable pageable
    );
}