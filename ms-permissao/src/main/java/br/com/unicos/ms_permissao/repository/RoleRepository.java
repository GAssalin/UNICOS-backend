package br.com.unicos.ms_permissao.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_permissao.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório da entidade {@link Role}, sempre restrito à empresa (tenant).
 */
@Repository
public interface RoleRepository extends BaseTenantRepository<Role, Long> {

    boolean existsByNomeIgnoreCaseAndEmpresaId(String nome, Long empresaId);

    List<Role> findByEmpresaIdOrderByNomeAsc(Long empresaId);
}
