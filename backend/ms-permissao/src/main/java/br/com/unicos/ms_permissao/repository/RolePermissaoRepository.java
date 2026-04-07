package br.com.unicos.ms_permissao.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_permissao.model.RolePermissao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link RolePermissao}.
 * <p>
 * Esta entidade representa o vínculo entre empresa, papel (role) e permissão,
 * sendo a base do modelo de autorização multi-tenant do UniCoS.
 * </p>
 *
 * <p>
 * As consultas são divididas em dois grupos:
 * <ul>
 *   <li><b>Administrativas</b>: utilizadas em telas e grids (paginadas)</li>
 *   <li><b>Técnicas</b>: utilizadas em tempo de autenticação/autorização (não paginadas)</li>
 * </ul>
 * </p>
 */
@Repository
public interface RolePermissaoRepository extends BaseTenantRepository<RolePermissao, Long> {

    /**
     * Verifica se já existe um vínculo entre empresa, papel e permissão.
     */
    boolean existsByRoleIdAndPermissaoIdAndEmpresaId(Long roleId, Long permissaoId, Long empresaId);

    List<RolePermissao> findByAtivoTrueAndEmpresaId(Long empresaId);
}
