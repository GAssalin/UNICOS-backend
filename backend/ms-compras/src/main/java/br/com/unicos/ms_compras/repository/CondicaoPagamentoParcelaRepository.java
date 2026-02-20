package br.com.unicos.ms_compras.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_compras.model.CondicaoPagamentoParcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link CondicaoPagamentoParcela}.
 * <p>
 * Centraliza consultas relacionadas às parcelas das condições de pagamento,
 * respeitando o contexto multi-tenant (empresa/tenant).
 * </p>
 */
@Repository
public interface CondicaoPagamentoParcelaRepository extends BaseTenantRepository<CondicaoPagamentoParcela, Long> {

    /**
     * Lista todas as parcelas de uma condição de pagamento dentro do tenant.
     */
    List<CondicaoPagamentoParcela> findByCondicaoPagamentoIdAndEmpresaIdOrderByOrdemAsc(
            Long condicaoPagamentoId,
            Long tenantId
    );

    /**
     * Lista parcelas paginadas de uma condição de pagamento dentro do tenant.
     */
    Page<CondicaoPagamentoParcela> findByCondicaoPagamentoIdAndEmpresaId(
            Long condicaoPagamentoId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera parcela específica pela ordem dentro de uma condição de pagamento e tenant.
     */
    Optional<CondicaoPagamentoParcela> findByCondicaoPagamentoIdAndOrdemAndEmpresaId(
            Long condicaoPagamentoId,
            Integer ordem,
            Long tenantId
    );

    /**
     * Verifica se já existe parcela com a mesma ordem dentro da condição de pagamento no tenant.
     */
    boolean existsByCondicaoPagamentoIdAndOrdemAndEmpresaId(
            Long condicaoPagamentoId,
            Integer ordem,
            Long tenantId
    );

    /**
     * Remove todas as parcelas de uma condição de pagamento dentro do tenant.
     */
    void deleteByCondicaoPagamentoIdAndEmpresaId(
            Long condicaoPagamentoId,
            Long tenantId
    );
}