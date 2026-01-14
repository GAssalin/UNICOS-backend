package br.com.unicos.ms_filial.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_filial.model.ContatoFilial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link ContatoFilial}.
 * <p>
 * Centraliza consultas relacionadas aos contatos de filiais,
 * respeitando o contexto multi-tenant.
 * </p>
 */
@Repository
public interface ContatoFilialRepository extends BaseTenantRepository<ContatoFilial, Long> {

    /**
     * Lista contatos por filial dentro do tenant com paginação.
     */
    Page<ContatoFilial> findByFilialIdAndEmpresaId(
            Long filialId,
            Long tenantId,
            Pageable pageable
    );

    /**
     * Recupera o contato por id e filial dentro do tenant.
     */
    Optional<ContatoFilial> findByIdAndFilialIdAndEmpresaId(
            Long id,
            Long filialId,
            Long tenantId
    );

    /**
     * Verifica se já existe contato com o mesmo e-mail principal na filial dentro do tenant.
     */
    boolean existsByFilialIdAndEmailPrincipalAndEmpresaId(
            Long filialId,
            String emailPrincipal,
            Long tenantId
    );

    /**
     * Remove todos os contatos de uma filial dentro do tenant.
     */
    void deleteByFilialIdAndEmpresaId(
            Long filialId,
            Long tenantId
    );
}
