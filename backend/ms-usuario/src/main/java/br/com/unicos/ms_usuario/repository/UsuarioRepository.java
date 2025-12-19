package br.com.unicos.ms_usuario.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_usuario.model.Usuario;
import jakarta.validation.constraints.NotBlank;
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
 * Este repositório contém apenas consultas relacionadas
 * ao ciclo de autenticação, autorização e administração de usuários.
 * </p>
 */
@Repository
public interface UsuarioRepository extends BaseTenantRepository<Usuario, Long> {

    // ============================================================
    // Consultas de autenticação (RUNTIME - NÃO PAGINADAS)
    // ============================================================

    /**
     * Busca um usuário pelo login dentro da empresa (tenant).
     * Apenas usuários com e-mail verificado são considerados.
     *
     * @param login     Login do usuário.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o usuário, caso exista e esteja apto a autenticar.
     */
    Optional<Usuario> findByLoginIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(String login, Long empresaId);

    /**
     * Busca um usuário pelo e-mail dentro da empresa (tenant).
     * Apenas usuários com e-mail verificado são considerados.
     *
     * @param email     E-mail do usuário.
     * @param empresaId Identificador da empresa (tenant).
     * @return {@link Optional} contendo o usuário, caso exista e esteja apto a autenticar.
     */
    Optional<Usuario> findByEmailIgnoreCaseAndEmailVerificadoTrueAndEmpresaId(String email, Long empresaId);

    // ============================================================
    // Consultas administrativas (PAGINADAS)
    // ============================================================

    /**
     * Lista usuários ativos com e-mail verificado dentro de uma empresa (tenant),
     * de forma paginada.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Informações de paginação e ordenação.
     * @return Página de usuários ativos e verificados.
     */
    Page<Usuario> findByAtivoTrueAndEmailVerificadoTrueAndEmpresaId(Long empresaId, Pageable pageable);

    /**
     * Lista usuários inativos com e-mail verificado dentro de uma empresa (tenant),
     * de forma paginada.
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Informações de paginação e ordenação.
     * @return Página de usuários inativos e verificados.
     */
    Page<Usuario> findByAtivoFalseAndEmailVerificadoTrueAndEmpresaId(Long empresaId, Pageable pageable);

    /**
     * Busca um usuário pelo email.
     *
     * @param email email do usuário
     * @return {@link Optional} contendo o usuário, caso exista com o email informado
     */
    Optional<Usuario> findByEmailIgnoreCase(@NotBlank String email);

    /**
     * Busca um usuário pelo login.
     *
     * @param login login do usuário
     * @return {@link Optional} contendo o usuário, caso exista com o login informado
     */
    Optional<Usuario> findByLoginIgnoreCase(@NotBlank String login);
}
