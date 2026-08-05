package br.com.unicos.ms_empresa.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_empresa.enums.PerfilEmpresaUsuario;
import br.com.unicos.ms_empresa.model.EmpresaUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link EmpresaUsuario}.
 */
@Repository
public interface EmpresaUsuarioRepository extends BaseTenantRepository<EmpresaUsuario, Long> {

    /**
     * Recupera o vínculo de um usuário dentro do tenant.
     *
     * @param usuarioId Identificador do usuário no ms-auth.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o vínculo, se existir.
     */
    Optional<EmpresaUsuario> findByUsuarioIdAndEmpresaId(Long usuarioId, Long empresaId);

    /**
     * Verifica se um usuário já está vinculado ao tenant.
     *
     * @param usuarioId Identificador do usuário no ms-auth.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@code true} se o vínculo existir; {@code false} caso contrário.
     */
    boolean existsByUsuarioIdAndEmpresaId(Long usuarioId, Long empresaId);

    /**
     * Lista os usuários vinculados ao tenant com suporte à paginação.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação.
     * @return Página de vínculos empresa-usuário.
     */
    Page<EmpresaUsuario> findByEmpresaId(Long empresaId, Pageable pageable);

    /**
     * Lista os usuários vinculados ao tenant filtrando pelo perfil.
     *
     * @param perfil    Perfil do usuário dentro da empresa.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Parâmetros de paginação.
     * @return Página de usuários filtrados por perfil.
     */
    Page<EmpresaUsuario> findByPerfilAndEmpresaId(
            PerfilEmpresaUsuario perfil,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Remove o vínculo de um usuário com o tenant.
     *
     * @param usuarioId Identificador do usuário no ms-auth.
     * @param empresaId Identificador da empresa (tenant).
     */
    void deleteByUsuarioIdAndEmpresaId(Long usuarioId, Long empresaId);
}