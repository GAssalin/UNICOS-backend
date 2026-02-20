package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.CondicaoPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link CondicaoPagamento}.
 * <p>
 * Centraliza consultas relacionadas a condições de pagamento utilizadas em compras,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface CondicaoPagamentoRepository extends BaseTenantRepository<CondicaoPagamento, Long> {

    /**
     * Lista condições de pagamento dentro do tenant com paginação.
     */
    Page<CondicaoPagamento> findAllByEmpresaId(
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera condição de pagamento por ID dentro do tenant.
     */
    Optional<CondicaoPagamento> findByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Recupera condição de pagamento por código dentro do tenant.
     */
    Optional<CondicaoPagamento> findByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Verifica se já existe condição de pagamento com o mesmo código dentro do tenant.
     */
    boolean existsByCodigoAndEmpresaId(
            String codigo,
            Long tenantId
    );

    /**
     * Verifica se existe condição de pagamento (por ID) dentro do tenant.
     */
    boolean existsByIdAndEmpresaId(
            Long id,
            Long tenantId
    );

    /**
     * Lista condições de pagamento por nome (contém, ignorando maiúsculas/minúsculas) dentro do tenant.
     */
    Page<CondicaoPagamento> findByNomeContainingIgnoreCaseAndEmpresaId(
            String nome,
            Long tenantId,
            Pageable pageable
    );
}