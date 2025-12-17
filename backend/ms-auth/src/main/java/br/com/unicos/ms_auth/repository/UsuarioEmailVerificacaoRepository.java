package br.com.unicos.ms_auth.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_auth.model.UsuarioEmailVerificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados relacionados ao processo de
 * verificação de e-mail do usuário.
 *
 * <p>
 * Em arquitetura multi-tenant, todos os tokens de verificação
 * são isolados por empresa (tenant), identificada pelo campo {@code empresaId}.
 * </p>
 *
 * <p>
 * Gerencia tokens temporários utilizados para confirmar o endereço de e-mail
 * durante o fluxo de criação de conta, bem como seus prazos de validade.
 * </p>
 */
@Repository
public interface UsuarioEmailVerificacaoRepository
        extends BaseTenantRepository<UsuarioEmailVerificacao, Long> {

    // ============================================================
    // Consultas de runtime (NÃO PAGINADAS)
    // ============================================================

    /**
     * Busca um registro de verificação pelo hash do token,
     * restrito à empresa (tenant).
     *
     * <p>
     * O token enviado ao usuário não é armazenado diretamente,
     * apenas seu hash, por questões de segurança.
     * </p>
     *
     * @param tokenHash Hash do token gerado.
     * @param empresaId Identificador da empresa (tenant).
     * @return Registro correspondente, caso exista no tenant.
     */
    Optional<UsuarioEmailVerificacao> findByTokenHashAndEmpresaId(
            String tokenHash,
            Long empresaId
    );

    /**
     * Busca um token válido (não expirado) dentro da empresa (tenant).
     *
     * @param tokenHash Hash do token.
     * @param agora     Data/hora atual para validação da expiração.
     * @param empresaId Identificador da empresa (tenant).
     * @return Registro válido, se encontrado no tenant.
     */
    Optional<UsuarioEmailVerificacao> findByTokenHashAndExpiracaoAfterAndEmpresaId(
            String tokenHash,
            LocalDateTime agora,
            Long empresaId
    );

    /**
     * Busca o token pendente mais recente para um usuário específico,
     * dentro da empresa (tenant).
     *
     * <p>
     * Utilizado para evitar geração duplicada de tokens
     * em fluxos de reenvio de confirmação de e-mail.
     * </p>
     *
     * @param usuarioId ID do usuário.
     * @param empresaId Identificador da empresa (tenant).
     * @return Token ainda não utilizado, se existir no tenant.
     */
    Optional<UsuarioEmailVerificacao> findByUsuarioIdAndUtilizadoFalseAndEmpresaId(
            Long usuarioId,
            Long empresaId
    );

    // ============================================================
    // Consultas administrativas / batch (PAGINADAS)
    // ============================================================

    /**
     * Lista tokens de verificação já expirados dentro de uma empresa (tenant),
     * de forma paginada.
     *
     * <p>
     * Utilizado em rotinas de limpeza periódica (jobs/batch).
     * </p>
     *
     * @param agora     Data/hora atual.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Informações de paginação.
     * @return Página de tokens expirados.
     */
    Page<UsuarioEmailVerificacao> findByExpiracaoBeforeAndEmpresaId(
            LocalDateTime agora,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Lista tokens ativos (não expirados) dentro de uma empresa (tenant),
     * de forma paginada.
     *
     * @param agora     Data/hora atual.
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Informações de paginação.
     * @return Página de tokens válidos.
     */
    Page<UsuarioEmailVerificacao> findByExpiracaoAfterAndEmpresaId(
            LocalDateTime agora,
            Long empresaId,
            Pageable pageable
    );

    /**
     * Lista tokens pendentes (não utilizados) dentro de uma empresa (tenant),
     * de forma paginada.
     *
     * <p>
     * Pode ser utilizado para auditoria ou rotinas administrativas.
     * </p>
     *
     * @param empresaId Identificador da empresa (tenant).
     * @param pageable  Informações de paginação.
     * @return Página de tokens pendentes.
     */
    Page<UsuarioEmailVerificacao> findByUtilizadoFalseAndEmpresaId(
            Long empresaId,
            Pageable pageable
    );
}
