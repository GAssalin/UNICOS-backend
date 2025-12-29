package br.com.unicos.ms_empresa.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_empresa.enums.PerfilEmpresaUsuario;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EmpresaUsuario}.
 * <p>
 * Centraliza consultas relacionadas ao vínculo entre usuários e empresas (tenants),
 * respeitando o contexto multi-tenant.
 * </p>
 *
 * <p>
 * Utilizado em fluxos como:
 * <ul>
 *     <li>Controle de acesso por empresa</li>
 *     <li>Definição de perfis e permissões</li>
 *     <li>Validação de vínculo usuário-empresa</li>
 * </ul>
 * </p>
 */
@Repository
public interface EmpresaUsuarioRepository extends BaseTenantRepository<EmpresaUsuario, Long> {

    /**
     * Recupera o vínculo entre um usuário e uma empresa dentro do tenant.
     *
     * @param empresa   Empresa vinculada.
     * @param usuarioId Identificador do usuário no ms-auth.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o vínculo, se existir.
     */
    Optional<EmpresaUsuario> findByEmpresaAndUsuarioIdAndEmpresaId(
            Empresa empresa,
            Long usuarioId,
            Long empresaId
    );

    /**
     * Verifica se um usuário já está vinculado a uma empresa dentro do tenant.
     *
     * @param empresa   Empresa vinculada.
     * @param usuarioId Identificador do usuário no ms-auth.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se o vínculo existir; {@code false} caso contrário.
     */
    boolean existsByEmpresaAndUsuarioIdAndEmpresaId(
            Empresa empresa,
            Long usuarioId,
            Long empresaId
    );

    /**
     * Lista todos os usuários vinculados a uma empresa dentro do tenant,
     * com suporte à paginação.
     *
     * @param empresa   Empresa vinculada.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação.
     * @return Página de vínculos empresa-usuário.
     */
    Page<EmpresaUsuario> findByEmpresaAndEmpresaId(
            Empresa empresa,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Lista os usuários vinculados a uma empresa filtrando pelo perfil,
     * respeitando o contexto multi-tenant.
     *
     * @param empresa   Empresa vinculada.
     * @param perfil    Perfil do usuário dentro da empresa.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação.
     * @return Página de usuários filtrados por perfil.
     */
    Page<EmpresaUsuario> findByEmpresaAndPerfilAndEmpresaId(
            Empresa empresa,
            PerfilEmpresaUsuario perfil,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Remove o vínculo de um usuário com uma empresa dentro do tenant.
     *
     * @param empresa   Empresa vinculada.
     * @param usuarioId Identificador do usuário no ms-auth.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByEmpresaAndUsuarioIdAndEmpresaId(
            Empresa empresa,
            Long usuarioId,
            Long empresaId
    );
}
