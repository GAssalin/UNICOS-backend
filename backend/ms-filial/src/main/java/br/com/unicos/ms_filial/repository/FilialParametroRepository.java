package br.com.unicos.ms_filial.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_filial.model.FilialParametro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link FilialParametro}.
 * <p>
 * Centraliza consultas relacionadas a parâmetros configuráveis por filial,
 * respeitando o contexto multi-tenant.
 * </p>
 */
@Repository
public interface FilialParametroRepository extends BaseTenantRepository<FilialParametro, Long> {

    /**
     * Lista parâmetros por filial dentro do tenant com paginação.
     */
    Page<FilialParametro> findByFilialIdAndEmpresaId(
            Long filialId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera parâmetro por chave e filial dentro do tenant.
     */
    Optional<FilialParametro> findByFilialIdAndChaveAndEmpresaId(
            Long filialId,
            String chave,
            Long tenantId
    );

    /**
     * Verifica se já existe parâmetro com a mesma chave na filial dentro do tenant.
     */
    boolean existsByFilialIdAndChaveAndEmpresaId(
            Long filialId,
            String chave,
            Long tenantId
    );

    /**
     * Remove todos os parâmetros de uma filial dentro do tenant.
     */
    void deleteByFilialIdAndEmpresaId(
            Long filialId,
            Long tenantId
    );
}
