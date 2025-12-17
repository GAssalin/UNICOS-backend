package br.com.unicos.ms_auth.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_auth.model.RoleHierarchyRelation;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link RoleHierarchyRelation}.
 * <p>
 * Representa a hierarquia entre papéis (roles) do sistema,
 * permitindo definir relações de herança de permissões
 * (ex.: ADMIN herda permissões de GERENTE).
 * </p>
 *
 * <p>
 * Trata-se de uma estrutura global de autorização,
 * compartilhada entre todos os tenants do UniCoS.
 * </p>
 */
@Repository
public interface RoleHierarchyRelationRepository extends BaseTenantRepository<RoleHierarchyRelation, Long> {
}
