package br.com.unicos.ms_pessoas.usuario.repository;

import br.com.unicos.core.tenant.repository.BaseTenantRepository;
import br.com.unicos.ms_pessoas.usuario.model.UsuarioEmailVerificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados relacionados ao processo de
 * verificação de e-mail do usuário.
 *
 * <p>
 * O token enviado ao usuário não é armazenado diretamente, apenas seu hash (SHA-256).
 * Como o token é aleatório, a busca pelo hash identifica o registro sem depender da empresa,
 * permitindo a confirmação por usuários ainda não autenticados.
 * </p>
 */
@Repository
public interface UsuarioEmailVerificacaoRepository extends BaseTenantRepository<UsuarioEmailVerificacao, Long> {

    /**
     * Busca um token válido (não expirado) pelo hash.
     */
    Optional<UsuarioEmailVerificacao> findByTokenHashAndExpiracaoAfter(String tokenHash, LocalDateTime agora);

    /**
     * Tokens ainda não utilizados de um usuário, dentro da empresa (tenant).
     */
    List<UsuarioEmailVerificacao> findByUsuarioIdAndUtilizadoFalseAndEmpresaId(Long usuarioId, Long empresaId);

    /**
     * Tokens expirados ainda não marcados como utilizados, dentro da empresa (tenant).
     */
    List<UsuarioEmailVerificacao> findByExpiracaoBeforeAndUtilizadoFalseAndEmpresaId(LocalDateTime agora, Long empresaId);

    /**
     * Lista tokens já expirados dentro de uma empresa (tenant), de forma paginada.
     */
    Page<UsuarioEmailVerificacao> findByExpiracaoBeforeAndEmpresaId(LocalDateTime agora, Long empresaId, Pageable pageable);

    /**
     * Lista tokens pendentes (não utilizados) dentro de uma empresa (tenant), de forma paginada.
     */
    Page<UsuarioEmailVerificacao> findByUtilizadoFalseAndEmpresaId(Long empresaId, Pageable pageable);
}
