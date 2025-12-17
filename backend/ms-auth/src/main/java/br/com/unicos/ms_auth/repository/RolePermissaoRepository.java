package br.com.unicos.ms_auth.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_auth.model.RolePermissao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    /**
     * Busca um vínculo específico entre empresa, papel e permissão.
     */
    Optional<RolePermissao> findByRoleIdAndPermissaoIdAndEmpresaId(Long empresaId, Long roleId, Long permissaoId);

    /**
     * Retorna os nomes das permissões ativas de uma empresa
     * para um conjunto de papéis (roles).
     *
     * <p>
     * Utilizado durante o processo de autenticação/autorização
     * em tempo de execução.
     * </p>
     */
    List<String> findAllByRoleIdAndPermissaoIdAndEmpresaId(Long roleId, Long permissaoId, Long empresaId);

    /**
     * Verifica se o usuário possui determinada permissão
     * considerando a empresa (tenant) atual.
     */
    @Query("""
                select count(erp) > 0
                  from RolePermissao erp
                  join erp.role r
                  join UsuarioRole ur on ur.role = r
                  join ur.usuario u
                  join erp.permissao p
                 where u.id = :usuarioId
                   and erp.empresaId = :empresaId
                   and erp.ativo = true
                   and p.nome = :permissao
            """)
    boolean usuarioPossuiPermissao(@Param("usuarioId") Long usuarioId, @Param("empresaId") Long empresaId, @Param("permissao") String nomePermissao);

    List<RolePermissao> findByAtivoTrueAndEmpresaId(Long empresaId);

    List<RolePermissao> findByAtivoFalseAndEmpresaId(Long empresaId);

}
