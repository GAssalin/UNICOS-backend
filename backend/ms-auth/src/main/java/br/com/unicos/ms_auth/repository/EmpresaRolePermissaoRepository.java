package br.com.unicos.ms_auth.repository;

import br.com.unicos.ms_auth.model.EmpresaRolePermissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade EmpresaRolePermissao.
 * <p>
 * Esta entidade representa o vínculo entre:
 * <ul>
 *   <li>Empresa</li>
 *   <li>Role (papel)</li>
 *   <li>Permissão</li>
 * </ul>
 *
 * <p>
 * É a base do modelo de autorização multi-tenant do UniCoS,
 * permitindo que cada empresa defina suas próprias regras de acesso
 * sem impactar outras empresas ou a plataforma como um todo.
 */
@Repository
public interface EmpresaRolePermissaoRepository
        extends JpaRepository<EmpresaRolePermissao, Long> {

    /**
     * Lista todas as permissões vinculadas a uma empresa,
     * independentemente de estarem ativas ou não.
     *
     * @param empresaId Identificador da empresa.
     * @return Lista de vínculos EmpresaRolePermissao.
     */
    List<EmpresaRolePermissao> findByEmpresaId(Long empresaId);

    /**
     * Lista todas as permissões ativas de uma empresa.
     *
     * @param empresaId Identificador da empresa.
     * @return Lista de permissões ativas da empresa.
     */
    List<EmpresaRolePermissao> findByEmpresaIdAndAtivoTrue(Long empresaId);

    /**
     * Lista as permissões ativas de uma empresa para um papel específico.
     *
     * @param empresaId Identificador da empresa.
     * @param roleId    Identificador do papel (role).
     * @return Lista de permissões ativas para o papel informado.
     */
    List<EmpresaRolePermissao> findByEmpresaIdAndRole_IdAndAtivoTrue(
            Long empresaId,
            Long roleId
    );

    /**
     * Verifica se já existe um vínculo entre empresa, papel e permissão.
     *
     * @param empresaId   Identificador da empresa.
     * @param roleId      Identificador do papel.
     * @param permissaoId Identificador da permissão.
     * @return {@code true} se o vínculo já existir, {@code false} caso contrário.
     */
    boolean existsByEmpresaIdAndRole_IdAndPermissao_Id(
            Long empresaId,
            Long roleId,
            Long permissaoId
    );

    /**
     * Busca um vínculo específico entre empresa, papel e permissão.
     *
     * @param empresaId   Identificador da empresa.
     * @param roleId      Identificador do papel.
     * @param permissaoId Identificador da permissão.
     * @return Optional contendo o vínculo, se existir.
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
     * Este método é utilizado principalmente durante o processo de autenticação
     * e autorização em tempo de execução, permitindo carregar as permissões
     * dinamicamente a partir do banco de dados.
     *
     * @param empresaId Identificador da empresa.
     * @param roles     Lista de nomes de papéis (ex: ADMIN, GERENTE).
     * @return Lista distinta de nomes de permissões.
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
     * Lista os vínculos de permissões de uma empresa
     * já carregando os dados de papel e permissão.
     *
     * <p>
     * Ideal para uso em telas administrativas (listagens, grids),
     * evitando problemas de N+1 queries.
     *
     * @param empresaId Identificador da empresa.
     * @return Lista de vínculos com role e permissão carregados.
     */
    @Query("""
                select erp
                from EmpresaRolePermissao erp
                join fetch erp.role
                join fetch erp.permissao
                where erp.empresaId = :empresaId
            """)
    List<EmpresaRolePermissao> listarComRoleEPermissao(
            @Param("empresaId") Long empresaId
    );
}
