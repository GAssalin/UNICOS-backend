package br.com.unicos.ms_auth.repository;

import br.com.unicos.ms_auth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelo acesso aos dados da entidade Usuario.
 * Mantém apenas consultas relacionadas ao próprio usuário.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo login.
     * Apenas usuários com e-mail verificado são retornados.
     */
    Optional<Usuario> findByLoginAndEmailVerificadoTrue(String login);

    /**
     * Busca um usuário pelo e-mail.
     * Apenas usuários com e-mail verificado são retornados.
     */
    Optional<Usuario> findByEmailAndEmailVerificadoTrue(String email);

    /**
     * Lista todos os usuários ativos que possuem e-mail verificado.
     */
    List<Usuario> findByAtivoTrueAndEmailVerificadoTrue();

    /**
     * Lista todos os usuários inativos que possuem e-mail verificado.
     */
    List<Usuario> findByAtivoFalseAndEmailVerificadoTrue();

    /**
     * Busca um usuário associado ao refresh token informado.
     */
    Optional<Usuario> findByRefreshToken(String refreshToken);
}
