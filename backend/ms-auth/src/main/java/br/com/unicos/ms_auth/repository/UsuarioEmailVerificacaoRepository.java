package br.com.unicos.ms_auth.repository;

import br.com.unicos.ms_auth.model.UsuarioEmailVerificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados relacionados ao processo de
 * verificação de e-mail do usuário.
 * <p>
 * Gerencia tokens temporários utilizados para confirmar o endereço de e-mail
 * durante o fluxo de criação de conta, bem como seus prazos de validade.
 */
@Repository
public interface UsuarioEmailVerificacaoRepository
        extends JpaRepository<UsuarioEmailVerificacao, Long> {

    /**
     * Busca um registro de verificação pelo hash do token.
     * O token enviado ao usuário não é armazenado diretamente,
     * apenas seu hash, por questões de segurança.
     *
     * @param tokenHash Hash do token gerado.
     * @return Registro correspondente, caso exista.
     */
    Optional<UsuarioEmailVerificacao> findByTokenHash(String tokenHash);

    /**
     * Busca um token válido (não expirado).
     *
     * @param tokenHash Hash do token.
     * @param agora     Data/hora atual para validação da expiração.
     * @return Registro válido, se encontrado.
     */
    Optional<UsuarioEmailVerificacao> findByTokenHashAndExpiracaoAfter(
            String tokenHash,
            LocalDateTime agora
    );

    /**
     * Lista todos os tokens já expirados.
     * Útil para rotinas de limpeza periódica.
     *
     * @param agora Data/hora atual.
     * @return Lista de tokens expirados.
     */
    List<UsuarioEmailVerificacao> findByExpiracaoBefore(LocalDateTime agora);

    /**
     * Lista todos os tokens ativos (não expirados).
     *
     * @param agora Data/hora atual.
     * @return Lista de tokens válidos.
     */
    List<UsuarioEmailVerificacao> findByExpiracaoAfter(LocalDateTime agora);

    /**
     * Busca o token pendente mais recente para um usuário específico.
     * Útil para evitar geração duplicada de tokens caso o usuário solicite
     * reenvio da confirmação de e-mail.
     *
     * @param usuarioId ID do usuário.
     * @return Token ainda não utilizado.
     */
    Optional<UsuarioEmailVerificacao> findByUsuarioIdAndUtilizadoFalse(Long usuarioId);

    /**
     * Lista todos os tokens pendentes (não utilizados).
     *
     * @return Lista de tokens pendentes.
     */
    List<UsuarioEmailVerificacao> findByUtilizadoFalse();
}
