package br.com.unicos.ms_auth.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_auth.model.EmpresaRolePermissao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EmpresaRolePermissao}.
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
public interface EmpresaRolePermissaoRepository
        extends BaseTenantRepository<EmpresaRolePermissao, Long> {

    // ============================================================
    // Consultas administrativas (PAGINADAS)
    // ============================================================

    /**
     * Lista todos os vínculos de permissões de uma empresa,
     * independentemente de estarem ativos ou não, de forma paginada.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Informações de paginação e ordenação.
     * @return Página de vínculos EmpresaRolePermissao.
     */
    Page<EmpresaRolePermissao> findByEmpresaId(
            Long empresaId,
            Pageable pageable
    );

    /**
     * Lista todas as permissões ativas de uma empresa,
     * de forma paginada.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Informações de paginação e ordenação.
     * @return Página de permissões ativas da empresa.
     */
    Page<EmpresaRolePermissao> findByEmpresaIdAndAtivoTrue(
            Long empresaId,
            Pageable pageable
    );

    /**
     * Lista as permissões ativas de uma empresa para um papel específico,
     * de forma paginada.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param roleId    Identificador do papel (role).
     * @param pageable  Informações de paginação e ordenação.
     * @return Página de permissões ativas para o papel informado.
     */
    Page<EmpresaRolePermissao> findByEmpresaIdAndRole_IdAndAtivoTrue(
            Long empresaId,
            Long roleId,
            Pageable pageable
    );

    /**
     * Lista os vínculos de permissões de uma empresa
     * já carregando os dados de papel e permissão,
     * de forma paginada.
     *
     * <p>
     * Ideal para uso em telas administrativas (listagens, grids),
     * evitando problemas de N+1 queries.
     * </p>
     *
     * @param empresaId Identificador da empresa.
     * @param pageable  Informações de paginação e ordenação.
     * @return Página de vínculos com role e permissão carregados.
     */
    @Query("""
                select erp
                from EmpresaRolePermissao erp
                join fetch erp.role
                join fetch erp.permissao
                where erp.empresaId = :empresaId
            """)
    Page<EmpresaRolePermissao> listarComRoleEPermissao(
            @Param("empresaId") Long empresaId,
            Pageable pageable
    );

    // ============================================================
    // Consultas técnicas (NÃO PAGINADAS)
    // ============================================================

    /**
     * Verifica se já existe um vínculo entre empresa, papel e permissão.
     */
    boolean existsByEmpresaIdAndRole_IdAndPermissao_Id(
            Long empresaId,
            Long roleId,
            Long permissaoId
    );

    /**
     * Busca um vínculo específico entre empresa, papel e permissão.
     */
    Optional<EmpresaRolePermissao> findByEmpresaIdAndRole_IdAndPermissao_Id(
            Long empresaId,
            Long roleId,
            Long permissaoId
    );

    /**
     * Retorna os nomes das permissões ativas de uma empresa
     * para um conjunto de papéis (roles).
     *
     * <p>
     * Utilizado durante o processo de autenticação/autorização
     * em tempo de execução.
     * </p>
     */
    @Query("""
                select distinct p.nome
                from EmpresaRolePermissao erp
                join erp.permissao p
                join erp.role r
                where erp.empresaId = :empresaId
                  and erp.ativo = true
                  and r.nome in :roles
            """)
    List<String> buscarPermissoesPorEmpresaERoles(
            @Param("empresaId") Long empresaId,
            @Param("roles") List<String> roles
    );

    /**
     * Verifica se o usuário possui determinada permissão
     * considerando a empresa (tenant) atual.
     */
    @Query("""
                select count(erp) > 0
                  from EmpresaRolePermissao erp
                  join erp.role r
                  join erp.permissao p
                 where erp.empresaId = :empresaId
                   and erp.ativo = true
                   and r.nome in :roles
                   and p.nome = :permissao
            """)
    boolean usuarioPossuiPermissao(
            @Param("usuarioId") Long usuarioId,
            @Param("empresaId") Long empresaId,
            @Param("permissao") String permissao
    );

    @Query("""
                select erp.id
                  from EmpresaRolePermissao erp
                 where erp.empresaId = :empresaId
            """)
    Page<Long> listarIdsPorEmpresa(
            @Param("empresaId") Long empresaId,
            Pageable pageable
    );

    @Query("""
                select erp
                  from EmpresaRolePermissao erp
                  join fetch erp.role
                  join fetch erp.permissao
                 where erp.id in :ids
            """)
    List<EmpresaRolePermissao> buscarComRoleEPermissaoPorIds(
            @Param("ids") List<Long> ids
    );

    @Query("""
                select erp.id
                  from EmpresaRolePermissao erp
                 where erp.empresaId = :empresaId
                   and erp.ativo = true
            """)
    Page<Long> listarIdsAtivosPorEmpresa(
            @Param("empresaId") Long empresaId,
            Pageable pageable
    );

}
