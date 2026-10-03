package br.com.unicos.ms_pessoas.usuario.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.usuario.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade {@link Usuario}.
 *
 * <p>
 * Em arquitetura multi-tenant, todos os usuários são isolados
 * por empresa (tenant), identificada pelo campo {@code empresaId}.
 * </p>
 *
 * <p>
 * Login e e-mail identificam o usuário na autenticação, que ocorre antes de se conhecer a
 * empresa; por isso as consultas e validações de unicidade desses campos são globais.
 * </p>
 */
@Repository
public interface UsuarioRepository extends BaseTenantRepository<Usuario, Long> {

    // ============================================================
    // Consultas globais (autenticação e unicidade)
    // ============================================================

    /**
     * Busca um usuário pelo e-mail, em qualquer empresa. Utilizado na autenticação.
     */
    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByLoginIgnoreCase(String login);

    // ============================================================
    // Consultas restritas à empresa (tenant)
    // ============================================================

    Optional<Usuario> findByLoginIgnoreCaseAndEmpresaId(String login, Long empresaId);

    /**
     * Lista usuários ativos com e-mail verificado dentro de uma empresa (tenant).
     */
    Page<Usuario> findByAtivoTrueAndEmailVerificadoTrueAndEmpresaId(Long empresaId, Pageable pageable);

    /**
     * Lista usuários inativos com e-mail verificado dentro de uma empresa (tenant).
     */
    Page<Usuario> findByAtivoFalseAndEmailVerificadoTrueAndEmpresaId(Long empresaId, Pageable pageable);
}
