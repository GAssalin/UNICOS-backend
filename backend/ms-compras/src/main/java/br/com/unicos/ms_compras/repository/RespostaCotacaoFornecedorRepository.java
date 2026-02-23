package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.RespostaCotacaoFornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link RespostaCotacaoFornecedor}.
 * <p>
 * Centraliza consultas relacionadas às respostas dos fornecedores para cotações,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface RespostaCotacaoFornecedorRepository extends BaseTenantRepository<RespostaCotacaoFornecedor, Long> {

    /**
     * Recupera resposta por ID dentro do tenant.
     */
    Optional<RespostaCotacaoFornecedor> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Recupera resposta por cotação e fornecedor dentro do tenant.
     */
    Optional<RespostaCotacaoFornecedor> findByCotacaoCompraIdAndFornecedorIdAndEmpresaId(
            Long cotacaoCompraId,
            Long fornecedorId,
            Long tenantId
    );

    /**
     * Verifica se fornecedor já respondeu a cotação dentro do tenant.
     */
    boolean existsByCotacaoCompraIdAndFornecedorIdAndEmpresaId(
            Long cotacaoCompraId,
            Long fornecedorId,
            Long tenantId
    );

    /**
     * Lista respostas por cotação dentro do tenant.
     */
    Page<RespostaCotacaoFornecedor> findByCotacaoCompraIdAndEmpresaId(
            Long cotacaoCompraId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista respostas por fornecedor dentro do tenant.
     */
    Page<RespostaCotacaoFornecedor> findByFornecedorIdAndEmpresaId(
            Long fornecedorId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista respostas por status dentro do tenant.
     */
    Page<RespostaCotacaoFornecedor> findByStatusAndEmpresaId(
            String status,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Lista respostas por condição de pagamento dentro do tenant.
     */
    Page<RespostaCotacaoFornecedor> findByCondicaoPagamentoIdAndEmpresaId(
            Long condicaoPagamentoId,
            Long tenantId,
            Pageable pageable
    );
}